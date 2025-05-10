package com.dev_bayan_ibrahim.brc_shifting.ui.screen.shedule

data class ScheduleLogicActions(
    val resetToday: () -> Unit,
    val selectPrevDay: () -> Unit,
    val selectNextDay: () -> Unit,
)

