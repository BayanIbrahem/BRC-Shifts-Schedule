package com.dev_bayan_ibrahim.brc_shifting.data_source.remote.endpoint

import com.dev_bayan_ibrahim.brc_shifting.util.months
import com.dev_bayan_ibrahim.brc_shifting.util.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus

enum class EndPointMonthVariance {
    ThisMonth,
    PreviousMonth;

    /**
     * get date of start of month
     */
    val date: LocalDate
        get() = LocalDateTime.now().date.let {
            val date = if (this == ThisMonth) {
                it
            } else {
                it.minus(1.months)
            }
            LocalDate(
                year = date.year,
                month = date.month,
                dayOfMonth = 1
            )
        }

    companion object Companion {
        fun getOrNull(monthNumber: Int, year: Int): EndPointMonthVariance? {
            val now = LocalDateTime.now().date
            val requiredMonthIndex = year.times(12).plus(monthNumber)
            val nowMonthIndex = now.year.times(12).plus(now.monthNumber)
            return when (nowMonthIndex - requiredMonthIndex) {
                0 -> ThisMonth
                1 -> PreviousMonth
                else -> null
            }
        }
    }
}