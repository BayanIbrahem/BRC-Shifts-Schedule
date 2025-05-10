package com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.state

import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.datetime.Instant

data class EmployeeMutateState(
    val isLoading: Boolean = false,
    val employeeName: String? = null,
    val employeeNumber: Int? = null,
    /**
     * disable this flag on edit
     */
    val isAdd: Boolean = true,
    val employeeGroup: WorkGroup? = null,
    val employeePassword: String? = null,
    val requestResult: Result<Employee>? = null,
    val visibleDialog: Int = -1,
    val employeeCreateAt: Instant? = null,
    val selectedEmployeesNumbers: Set<Int> = emptySet(),
) {
    val validLocalNewEmployee: Boolean
        get() = !employeeName.isNullOrBlank() && employeeNumber != null && employeeNumber > 0
    val validRemoteNewEmployee: Boolean
        get() = employeeNumber != null && employeeNumber > 0 && !employeePassword.isNullOrBlank()
    val showRemoteEmployeeDialog: Boolean
        get() = visibleDialog == DIALOG_ADD_REMOTE
    val showLocalEmployeeDialog: Boolean
        get() = visibleDialog == DIALOG_ADD_LOCAL

    val showAnyDialog: Boolean
        get() = showRemoteEmployeeDialog || showLocalEmployeeDialog

    val isSelectionMode: Boolean
        get() = selectedEmployeesNumbers.isNotEmpty()
    companion object {
        const val DIALOG_ADD_REMOTE = 0
        const val DIALOG_ADD_LOCAL = 1
    }
}
