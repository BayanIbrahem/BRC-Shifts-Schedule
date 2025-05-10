package com.dev_bayan_ibrahim.brc_shifting.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime

fun LocalDate.format() = "$year/$monthNumber/$dayOfMonth"
fun LocalDate.monthYearFormat() = "$year-$monthNumber"
fun Instant.format() = toLocalDateTime(TimeZone.currentSystemDefault()).date.format()
fun Instant.monthYearFormat() = toLocalDateTime(TimeZone.currentSystemDefault()).date.monthYearFormat()
