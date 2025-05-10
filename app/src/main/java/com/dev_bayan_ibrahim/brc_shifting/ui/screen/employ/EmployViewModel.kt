package com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.ScheduleManager
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee
import com.dev_bayan_ibrahim.brc_shifting.domain.model.util.arContains
import com.dev_bayan_ibrahim.brc_shifting.domain.repo.EmployeeRepo
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.actions.EmployeeLogicActions
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.state.EmployeeFilterState
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.state.EmployeeMutateState
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.state.EmployeeSortBy
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.state.EmployeeSortState
import com.dev_bayan_ibrahim.brc_shifting.util.Shift
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import com.dev_bayan_ibrahim.brc_shifting.util.now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
class EmployViewModel @Inject constructor(
    private val repo: EmployeeRepo,
    private val scheduleManager: ScheduleManager<Shift>,
) : ViewModel() {
    private val _mutateState = MutableStateFlow(EmployeeMutateState())
    val mutateState = _mutateState.asStateFlow()

    private val _sortState = MutableStateFlow(EmployeeSortState())
    val sortState = _sortState.asStateFlow()

    private val _filterState = MutableStateFlow(EmployeeFilterState())
    val filterState = _filterState.asStateFlow()

    val scheduleOfToday = flow {
        while (true) {
            val date = LocalDateTime.now().date
            val todaySchedules = WorkGroup.entries.associateWith { group ->
                runCatching {
                    scheduleManager.getShift(group = group, date = date)
                }.getOrNull()
            }.filterValues {
                it != null
            }.mapValues {
                it.value!!
            }
            emit(todaySchedules)
            delay(1.minutes)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
    )

    val allEmployeesFlow = combine(repo.getAllEmployees(), _sortState, _filterState) { employees, sort, filter ->
        employees.filter { employee ->
            val validQuery = filter.query.isNotBlank()
            val matchQuery = !validQuery || employee.name.arContains(filter.query)
            val matchGroup = filter.groups.isEmpty() || employee.group in filter.groups
            matchGroup && matchQuery
        }.let { filteredEmployees ->
            if (sort.asc) {
                when (sort.sortBy) {
                    EmployeeSortBy.NUMBER -> filteredEmployees.sortedBy { it.employeeNumber }
                    EmployeeSortBy.CREATE_DATE -> filteredEmployees.sortedBy { it.createdAt }
                    EmployeeSortBy.UPDATE_DATE -> filteredEmployees.sortedBy { it.updatedAt }
                    EmployeeSortBy.NAME -> filteredEmployees.sortedBy { it.name }
                    EmployeeSortBy.GROUP -> filteredEmployees.sortedBy { it.group?.orderNumber }
                }
            } else {
                when (sort.sortBy) {
                    EmployeeSortBy.NUMBER -> filteredEmployees.sortedByDescending { it.employeeNumber }
                    EmployeeSortBy.CREATE_DATE -> filteredEmployees.sortedByDescending { it.createdAt }
                    EmployeeSortBy.UPDATE_DATE -> filteredEmployees.sortedByDescending { it.updatedAt }
                    EmployeeSortBy.NAME -> filteredEmployees.sortedByDescending { it.name }
                    EmployeeSortBy.GROUP -> filteredEmployees.sortedByDescending { it.group?.orderNumber }
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun getLogicActions() = EmployeeLogicActions(
        onAddRemoteEmployee = {
            _mutateState.update { state ->
                if (state.showAnyDialog) {
                    state
                } else {
                    state.copy(visibleDialog = EmployeeMutateState.DIALOG_ADD_REMOTE)
                }
            }
        },
        onAddLocalEmployee = {
            _mutateState.update { state ->
                if (state.showAnyDialog) {
                    state
                } else {
                    state.copy(visibleDialog = EmployeeMutateState.DIALOG_ADD_LOCAL)
                }
            }
        },
        onConfirmAddRemoteEmployee = {
            val state = mutateState.value
            if (state.isLoading) return@EmployeeLogicActions
            if (state.validRemoteNewEmployee) {
                val number = state.employeeNumber?.takeIf { it > 0 } ?: return@EmployeeLogicActions
                val pw = state.employeePassword ?: return@EmployeeLogicActions
                _mutateState.update {
                    it.copy(isLoading = true, requestResult = null)
                }
                viewModelScope.launch {
                    val result = repo.getRemoteEmployee(
                        employNumber = number,
                        password = pw,
                        employeeGroup = state.employeeGroup
                    )
                    withContext(Dispatchers.Main.immediate) {
                        _mutateState.value = EmployeeMutateState()
                    }
                }
            }

        },
        onConfirmAddLocalEmployee = {
            val state = mutateState.value
            if (state.isLoading) return@EmployeeLogicActions
            if (state.validLocalNewEmployee) {
                val name = state.employeeName?.takeIf { it.isNotBlank() } ?: return@EmployeeLogicActions
                val number = state.employeeNumber?.takeIf { it > 0 } ?: return@EmployeeLogicActions
                _mutateState.update {
                    it.copy(isLoading = true, requestResult = null)
                }
                val now = Clock.System.now()
                val employee = Employee(
                    name = name,
                    employeeNumber = number,
                    group = state.employeeGroup,
                    remoteId = "",
                    createdAt = state.employeeCreateAt ?: now,
                    updatedAt = now,
                )
                viewModelScope.launch {
                    val result = repo.addEmployee(employee, override = true)
                    withContext(Dispatchers.Main.immediate) {
                        _mutateState.value = EmployeeMutateState()
                    }
                }
            }
        },
        onDeleteEmployee = { employee ->
            viewModelScope.launch {
                repo.deleteEmployee(employee.employeeNumber)
            }
        },
        onDeleteAllEmployees = {
            viewModelScope.launch {
                repo.deleteAllEmployees()
            }
        },
        onDeleteSelectedEmployees = {
            val state = mutateState.value
            if (state.selectedEmployeesNumbers.isNotEmpty()) {
                viewModelScope.launch {
                    repo.deleteEmployees(state.selectedEmployeesNumbers)
                }
                _mutateState.update { state ->
                    state.copy(selectedEmployeesNumbers = emptySet())
                }
            }
        },
        onCancelSelectEmployees = {
            _mutateState.update { state ->
                state.copy(selectedEmployeesNumbers = emptySet())
            }
        },
        onToggleSelectEmployee = { employee: Employee ->
            val selected = mutateState.value.selectedEmployeesNumbers
            val wasSelected = employee.employeeNumber in selected
            _mutateState.update {
                if (wasSelected) {
                    it.copy(selectedEmployeesNumbers = selected - employee.employeeNumber)
                } else {
                    it.copy(selectedEmployeesNumbers = selected + employee.employeeNumber)
                }
            }
        },
        onSetSelectedEmployees = { employees ->
            _mutateState.update {
                it.copy(
                    selectedEmployeesNumbers = employees.map(Employee::employeeNumber).toSet()
                )
            }
        },
        onLocalUpdateEmployee = { employee ->
            _mutateState.update {
                it.copy(
                    visibleDialog = EmployeeMutateState.DIALOG_ADD_LOCAL,
                    employeeName = employee.name,
                    isAdd = false,
                    employeeNumber = employee.employeeNumber,
                    employeeGroup = employee.group,
                    employeeCreateAt = employee.createdAt,
                )
            }

        },
        onRemoteUpdateEmployee = { employee ->
            _mutateState.update {
                it.copy(
                    visibleDialog = EmployeeMutateState.DIALOG_ADD_REMOTE,
                    employeeNumber = employee.employeeNumber,
                )
            }
        },
        onDismissAddMutateEmployee = {
            _mutateState.value = EmployeeMutateState()
        },
        onUpdateQuery = { query: String ->
            _filterState.update { it.copy(query = query) }
        },
        onToggleSelectFilterGroup = { group: WorkGroup ->
            val state = filterState.value
            val wasSelected = group in state.groups
            _filterState.update {
                if (wasSelected) {
                    it.copy(groups = state.groups - group)
                } else {
                    it.copy(groups = state.groups + group)
                }
            }
        },
        onToggleSortBy = { sortBy: EmployeeSortBy ->
            _sortState.update { it.copy(sortBy = sortBy) }
        },
        onToggleSortOrder = { asc: Boolean ->
            _sortState.update { it.copy(asc = asc) }
        },
        onEmployeeNameChange = { name ->
            _mutateState.update {
                it.copy(employeeName = name)
            }
        },
        onEmployeeNumberChange = { number ->
            _mutateState.update {
                it.copy(employeeNumber = number)
            }
        },
        onEmployeeGroupChange = { group ->
            _mutateState.update {
                it.copy(employeeGroup = group)
            }
        },
        onEmployeePasswordChange = { password ->
            _mutateState.update {
                it.copy(employeePassword = password)
            }
        },
    )

}