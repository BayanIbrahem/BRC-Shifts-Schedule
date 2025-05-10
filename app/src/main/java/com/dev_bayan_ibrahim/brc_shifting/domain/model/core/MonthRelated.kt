package com.dev_bayan_ibrahim.brc_shifting.domain.model.core

import kotlinx.datetime.LocalDate

/**
 * any thing related to a specific month in a year
 * @property monthNumber from 1 to 12
 * @property year the year like 2025
 */
interface MonthRelated {
    val monthNumber: Int
    val year: Int
    val atStartOfMonth get() = LocalDate(
        year = year,
        monthNumber = monthNumber,
        dayOfMonth = 1
    )
}