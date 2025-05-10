package com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.EmployeeSalary
import com.dev_bayan_ibrahim.brc_shifting.domain.repo.SalaryRepo
import com.dev_bayan_ibrahim.brc_shifting.ui.navigate.BRCDestination
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.action.SalaryLogicActions
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.state.SalaryUiState
import com.dev_bayan_ibrahim.brc_shifting.util.now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
class SalaryViewModel @Inject constructor(
    private val repo: SalaryRepo,
) : ViewModel() {
    private val _employeeNumberFlow: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _uiState: MutableStateFlow<SalaryUiState> = MutableStateFlow(SalaryUiState())
    val uiState = _uiState.asStateFlow()

    val thisMonthFlow: StateFlow<LocalDate?> = flow {
        while (true) {
            val now = LocalDateTime.now().date
            emit(now)

            delay(1.minutes)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val salaries: StateFlow<List<EmployeeSalary>> = _employeeNumberFlow.flatMapConcat { employeeNumber ->
        if (employeeNumber == null) flow { emit(emptyList()) } else {
            repo.getEmployeeSalaries(employeeNumber)
        }
    }.combine(
        _uiState.distinctUntilChangedBy { it.acsOrder }
    ) { salaries, state ->
        if (state.acsOrder) {
            salaries.sortedBy { it.atStartOfMonth }
        } else {
            salaries.sortedByDescending { it.atStartOfMonth }
        }
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun initSalaries(args: BRCDestination.Salary) {
        val number = args.employeeNumber
        _employeeNumberFlow.value = number
        viewModelScope.launch {
            val employee = repo.getEmployee(number)
            _uiState.value = SalaryUiState(employee = employee)
        }
    }

    fun buildLogicActions() = SalaryLogicActions(
        onFetchThisMonth = {
            fetchSalary(true)
        },
        onFetchPrevMonth = {
            fetchSalary(false)

        },
        onDeleteSalary = {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(requestResult = null)
                }
                repo.deleteSalary(it.employeeNumber, it.monthNumber, it.year)

                _uiState.update {
                    it.copy(requestResult = null)
                }
            }
        },
        onToggleOrder = { asc ->
            _uiState.update {
                it.copy(acsOrder = asc)
            }
        }
    )

    private fun fetchSalary(thisMonth: Boolean) {
        val state = uiState.value
        state.employee ?: return
        if ((state.prevMonthRequestLoading && !thisMonth) || (state.thisMonthRequestLoading && thisMonth)) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                if (thisMonth) {
                    it.copy(thisMonthRequestLoading = true, requestResult = null)
                } else {
                    it.copy(prevMonthRequestLoading = true, requestResult = null)
                }
            }
            val result = repo.getEmployeeRemoteSalary(state.employee.employeeNumber, thisMonth)
            _uiState.update {
                if (thisMonth) {
                    it.copy(thisMonthRequestLoading = false, requestResult = result)
                } else {
                    it.copy(prevMonthRequestLoading = false, requestResult = result)
                }
            }
        }
    }
}