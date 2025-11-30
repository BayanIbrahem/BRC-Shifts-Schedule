package com.dev_bayan_ibrahim.brc_shifting.ui.screen.deduction

import com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint.EndPointMonthVariance
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Deduction
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee

data class DeductionsUiState(
    val explicitMonth: Int? = null,
    val explicitYear: Int? = null,
    val thisMonth: Int = 0,
    val thisYear: Int = 0,
    val employee: Employee? = null,
    val isLoading: Boolean = false,
    val deductions: List<Deduction> = emptyList(),
) {
    val month: Int = explicitMonth ?: thisMonth
    val year: Int = explicitYear ?: thisYear
    val currentDateVariant: EndPointMonthVariance? = EndPointMonthVariance.getOrNull(
        monthNumber = month,
        year = year
    )
    val refreshable: Boolean = currentDateVariant != null
    val deductionsSum: Int = deductions.sumOf { it.monthlyInstallment }
}

