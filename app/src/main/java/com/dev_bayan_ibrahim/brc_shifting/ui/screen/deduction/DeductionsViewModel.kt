package com.dev_bayan_ibrahim.brc_shifting.ui.screen.deduction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.brc_shifting.domain.repo.DeductionsRepo
import com.dev_bayan_ibrahim.brc_shifting.domain.repo.EmployeeRepo
import com.dev_bayan_ibrahim.brc_shifting.ui.navigate.BRCDestination
import com.dev_bayan_ibrahim.brc_shifting.util.now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
class DeductionsViewModel @Inject constructor(
    private val employeeRepo: EmployeeRepo,
    private val deductionsRepo: DeductionsRepo,
) : ViewModel() {
    private val _employeeNumberFlow: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _uiState: MutableStateFlow<DeductionsUiState> = MutableStateFlow(DeductionsUiState())

    private val thisMonthFlow: StateFlow<LocalDate> = flow {
        while (true) {
            val now = LocalDateTime.now().date
            emit(now)
            delay(1.minutes)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LocalDateTime.now().date
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<DeductionsUiState> = combine(
        _employeeNumberFlow.flatMapLatest {
            it?.let {
                employeeRepo.getEmployee(it)
            } ?: flow { emit(null) }
        },
        thisMonthFlow,
        _uiState,
    ) { employee, now, uiState ->
        uiState.copy(
            employee = employee,
            thisMonth = now.monthNumber,
            thisYear = now.year,
        )
    }.map { uiState ->
        val deductions = if (uiState.employee == null) {
            emptyList()
        } else {
            deductionsRepo.getEmployeeDeductions(
                uiState.employee.employeeNumber,
                uiState.month,
                uiState.year,
                uiState.month,
                uiState.year,
            ).first().let {
                it.map { it.deductions }.flatten()
            }
        }
        uiState.copy(deductions = deductions)
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), DeductionsUiState()
    )

    fun initDeductions(args: BRCDestination.Deductions) {
        val number = args.employeeNumber
        _employeeNumberFlow.value = number
    }

    fun buildLogicActions() = DeductionsLogicActions(
        onRefresh = ::fetchDeductions,
        onSelectMonth = { month: Int? ->
            _uiState.update {
                it.copy(explicitMonth = month?.takeIf { it in 1..12 })
            }

        },
        onSelectyear = { year: Int? ->
            _uiState.update {
                it.copy(explicitYear = year?.takeIf { it in 2020..2080 })
            }
        },
        onResetDate = {
            _uiState.update {
                it.copy(
                    explicitYear = null,
                    explicitMonth = null
                )
            }
        },
    )

    private fun fetchDeductions() {
        val state = uiState.value
        state.employee ?: return
        if (state.isLoading) return
        if (state.currentDateVariant == null) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = deductionsRepo.getRemoteDeductions(
                employeeNumber = state.employee.employeeNumber,
                variances = setOf(state.currentDateVariant)
            )
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }
}