package com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.state

import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee
import com.dev_bayan_ibrahim.brc_shifting.domain.model.salary.EmployeeSalary

data class SalariesUiState(
    val employee: Employee? = null,
    val acsOrder: Boolean = false,
    val thisMonthRequestLoading: Boolean = false,
    val prevMonthRequestLoading: Boolean = false,
    val requestResult: Result<EmployeeSalary>? = null,
    val verticalView: Boolean = true,
)
