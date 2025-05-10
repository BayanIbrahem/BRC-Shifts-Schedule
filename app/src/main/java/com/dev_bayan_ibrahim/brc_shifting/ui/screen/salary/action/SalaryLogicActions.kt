package com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.action

import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.EmployeeSalary

data class SalaryLogicActions (
    val onFetchThisMonth: () -> Unit,
    val onFetchPrevMonth: () -> Unit,
    val onDeleteSalary: (EmployeeSalary) -> Unit,
    val onToggleOrder: (asc: Boolean)  -> Unit,
)