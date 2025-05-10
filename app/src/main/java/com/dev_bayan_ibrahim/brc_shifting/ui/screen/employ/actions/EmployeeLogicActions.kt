package com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.actions

import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.state.EmployeeSortBy
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup

typealias EmployeeCallback = (employee: Employee) -> Unit

data class EmployeeLogicActions(
    val onAddRemoteEmployee: () -> Unit,
    val onAddLocalEmployee: () -> Unit,
    val onConfirmAddRemoteEmployee: () -> Unit,
    val onConfirmAddLocalEmployee: () -> Unit,
    val onDismissAddMutateEmployee: () -> Unit,
    val onDeleteSelectedEmployees: () -> Unit,
    val onCancelSelectEmployees: () -> Unit,
    val onDeleteAllEmployees: () -> Unit,
    val onDeleteEmployee: EmployeeCallback,
    val onToggleSelectEmployee: EmployeeCallback,
    val onSetSelectedEmployees: (Collection<Employee>) -> Unit,
    val onLocalUpdateEmployee: EmployeeCallback,
    val onRemoteUpdateEmployee: EmployeeCallback,
    val onUpdateQuery: (String) -> Unit,
    val onToggleSelectFilterGroup: (WorkGroup) -> Unit,
    val onToggleSortBy: (EmployeeSortBy) -> Unit,
    val onToggleSortOrder: (Boolean) -> Unit,
    val onEmployeeNameChange: (String) -> Unit,
    val onEmployeeNumberChange: (Int) -> Unit,
    val onEmployeeGroupChange: (WorkGroup) -> Unit,
    val onEmployeePasswordChange: (String) -> Unit,
)