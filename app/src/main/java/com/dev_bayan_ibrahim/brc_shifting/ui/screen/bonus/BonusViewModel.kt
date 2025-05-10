package com.dev_bayan_ibrahim.brc_shifting.ui.screen.bonus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Bonus
import com.dev_bayan_ibrahim.brc_shifting.domain.repo.BonusRepo
import com.dev_bayan_ibrahim.brc_shifting.ui.navigate.BRCDestination
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.bonus.action.BonusLogicActions
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.bonus.state.BonusUiState
import com.dev_bayan_ibrahim.brc_shifting.util.now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

private const val TAG = "BONUS"

@HiltViewModel
class BonusViewModel @Inject constructor(
    private val repo: BonusRepo,
) : ViewModel() {
    private val _employeeNumberFlow: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _uiState: MutableStateFlow<BonusUiState> = MutableStateFlow(BonusUiState())
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val employeeBonusList = _employeeNumberFlow.flatMapConcat { employeeNumber ->
        if (employeeNumber == null) flow { emit(emptyList<Bonus>()) } else {
            repo.getEmployeeBonus(employeeNumber)
        }
    }

    val bonusList: StateFlow<List<Bonus>> = employeeBonusList.combine(
        _uiState.distinctUntilChangedBy { Pair(it.acsOrder, it.selectedBonusTypes) }
    ) { bonuses, state ->
        if (state.selectedBonusTypes.isEmpty()) {
            bonuses
        } else {
            bonuses.filter { it.type in state.selectedBonusTypes }
        }.let { filteredBonuses ->
            if (state.acsOrder) {
                filteredBonuses.sortedBy { it.date }
            } else {
                filteredBonuses.sortedByDescending { it.date }
            }
        }
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
    val bonusTypeSumAndCount = bonusList.map {
        it.groupBy { it.type }.mapValues { (type, list) ->
            Pair(list.count(), list.sumOf { it.net })
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())


    fun initBonus(args: BRCDestination.Bonus) {
        val number = args.employeeNumber
        _employeeNumberFlow.value = number
        viewModelScope.launch {
            val employee = repo.getEmployee(number)
            _uiState.update {
                it.copy(employee = employee)
            }
            val lastFetchEpochDays = repo
                .lastBonusFetch(number)
                .getOrNull()
                ?.toLocalDateTime(TimeZone.currentSystemDefault())
                ?.date
                ?.toEpochDays()
            val nowEpochDays = LocalDateTime.now().date.toEpochDays()
            if (lastFetchEpochDays == null || lastFetchEpochDays < nowEpochDays) {
                onFetchBonus()
            }
        }
    }

    fun buildLogicActions() = BonusLogicActions(
        onFetch = {
            onFetchBonus()
        },
        onDeleteBonus = {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(requestResult = null)
                }
                it.id?.let { id ->
                    repo.deleteBonus(id)
                }
            }
        },
        onToggleSelectBonus = { bonus ->
            val selectedList = uiState.value.selectedBonus
            val isSelected = bonus.id in selectedList
            _uiState.update {
                if (isSelected) {
                    it.copy(
                        selectedBonus = selectedList - bonus.id!!
                    )
                } else {
                    it.copy(selectedBonus = selectedList + bonus.id!!)
                }
            }
        },

        onToggleSelectBonusType = { type ->
            val selectedList = uiState.value.selectedBonusTypes
            val isSelected = type in selectedList
            _uiState.update {
                if (isSelected) {
                    it.copy(selectedBonusTypes = selectedList - type)
                } else {
                    it.copy(selectedBonusTypes = selectedList + type)
                }
            }
        },
        onToggleOrder = { asc ->
            _uiState.update {
                it.copy(acsOrder = asc)
            }
        },
        onCancelSelectBonus = {
            _uiState.update {
                it.copy(selectedBonus = emptySet())
            }
        },
        onSetSelectedBonuses = { list ->
            _uiState.update {
                it.copy(selectedBonus = list.mapNotNull { it.id }.toSet())
            }
        },
        onDeleteSelectedBonuses = {
            val selected = uiState.value.selectedBonus
            viewModelScope.launch {
                repo.deleteBonusList(selected)
            }
            _uiState.update {
                it.copy(selectedBonus = emptySet())
            }

        },
        onDeleteAllBonuses = {
            val number = uiState.value.employee?.employeeNumber ?: return@BonusLogicActions
            viewModelScope.launch {
                repo.deleteEmployeeBonus(number)
            }
        },
    )

    private fun onFetchBonus() {
        val state = uiState.value
        if (state.employee != null && !state.isLoading) {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(isLoading = true, requestResult = null)
                }
                val result = repo.getEmployeeRemoteBonus(state.employee.employeeNumber)
                _uiState.update {
                    it.copy(isLoading = false, requestResult = result)
                }
            }
        }
    }
}