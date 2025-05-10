package com.dev_bayan_ibrahim.brc_shifting.util

import com.dev_bayan_ibrahim.brc_shifting.domain.model.util.SimpleDate
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.formatDate() = "$year/$monthNumber/$dayOfMonth ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
fun LocalDate.formatDate() = "$year/$monthNumber/$dayOfMonth"
fun SimpleDate.formatDate() = "$year/$monthNumber/$dayOfMonth"
fun LocalDate.monthYearFormat() = "$year-$monthNumber"
fun Instant.formatDate() = toLocalDateTime(TimeZone.currentSystemDefault()).date.formatDate()
fun Instant.formatDateTime() = toLocalDateTime(TimeZone.currentSystemDefault()).formatDate()
fun Instant.monthYearFormat() = toLocalDateTime(TimeZone.currentSystemDefault()).date.monthYearFormat()

