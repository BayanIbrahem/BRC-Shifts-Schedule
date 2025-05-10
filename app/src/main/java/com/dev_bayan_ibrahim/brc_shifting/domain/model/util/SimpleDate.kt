package com.dev_bayan_ibrahim.brc_shifting.domain.model.util

import kotlinx.datetime.LocalDate

/**
 * like [LocalDate] but can be used in other fields
 * @property [date] return the [LocalDate]
 * @property [daysSinceEpoch]  integer of days
 * @property toString in format `YYYY-MM-DD`
 */
data class SimpleDate(
    val year: Int,
    val monthNumber: Int,
    val dayOfMonth: Int,
) : Comparable<SimpleDate> {
    constructor(date: LocalDate) : this(
        year = date.year,
        monthNumber = date.monthNumber,
        dayOfMonth = date.dayOfMonth
    )

    val date: LocalDate
        get() = LocalDate(
            year = year,
            monthNumber = monthNumber,
            dayOfMonth = dayOfMonth
        )
    val daysSinceEpoch: Int
        get() = date.toEpochDays()

    companion object Companion {
        operator fun invoke(daysSinceEpoch: Int): SimpleDate {
            return SimpleDate(LocalDate.fromEpochDays(daysSinceEpoch))
        }

        operator fun invoke(date: String): SimpleDate {
            val tokens = date.split('-').map { it.toInt() }
            return SimpleDate(
                year = tokens[0],
                monthNumber = tokens[1],
                dayOfMonth = tokens[2]
            )
        }
    }

    override fun compareTo(other: SimpleDate): Int {
        return sequenceOf(
            year.compareTo(other.year),
            monthNumber.compareTo(other.monthNumber),
            dayOfMonth.compareTo(other.dayOfMonth)
        ).firstOrNull { it != 0 } ?: 0
    }

    override fun toString(): String = "$year-$monthNumber-$dayOfMonth"
}