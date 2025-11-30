package com.dev_bayan_ibrahim.brc_shifting.util

import com.dev_bayan_ibrahim.brc_shifting.domain.model.util.SimpleDate
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.format.TextStyle
import java.util.Locale

fun LocalDateTime.formatDate() = buildString {
    append(
        this@formatDate.dayOfWeek.getDisplayName(
            TextStyle.SHORT_STANDALONE,
            Locale.getDefault()
        )
    )
    append(" ")
    append(year)
    append("/")
    append(monthNumber)
    append("/")
    append(dayOfMonth)
    append(" ")
    append(hour.toString().padStart(2, '0'))
    append(":")
    append(minute.toString().padStart(2, '0'))
}

fun LocalDate.formatDate() = buildString {
    append(
        this@formatDate.dayOfWeek.getDisplayName(
            TextStyle.SHORT_STANDALONE,
            Locale.getDefault()
        )
    )
    append(" ")
    append(year)
    append("/")
    append(monthNumber)
    append("/")
    append(dayOfMonth)
}

fun SimpleDate.formatDate() = LocalDate(year, monthNumber, dayOfMonth).formatDate()

fun LocalDate.monthYearFormat() = "$year-$monthNumber"

fun Instant.formatDate() = toLocalDateTime(TimeZone.currentSystemDefault()).date.formatDate()
fun Instant.formatDateTime() = toLocalDateTime(TimeZone.currentSystemDefault()).formatDate()
fun Instant.monthYearFormat() = toLocalDateTime(TimeZone.currentSystemDefault()).date.monthYearFormat()