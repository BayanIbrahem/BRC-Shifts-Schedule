package com.dev_bayan_ibrahim.brc_shifting.ui.screen.shedule

import com.dev_bayan_ibrahim.brc_shifting.util.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class ScheduleUiState(
    val selectedDate: LocalDate = LocalDateTime.now().date,
)
