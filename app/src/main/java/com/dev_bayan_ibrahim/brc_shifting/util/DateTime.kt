package com.dev_bayan_ibrahim.brc_shifting.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun LocalDateTime.Companion.now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

val Int.days: DatePeriod get() = DatePeriod(days = this)
val Int.months: DatePeriod get() = DatePeriod(months = this)
val Int.years: DatePeriod get() = DatePeriod(years = this)
