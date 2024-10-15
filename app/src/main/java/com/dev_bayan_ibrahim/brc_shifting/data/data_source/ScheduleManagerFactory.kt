package com.dev_bayan_ibrahim.brc_shifting.data.data_source

import com.dev_bayan_ibrahim.brc_shifting.util.Shift
import kotlinx.datetime.LocalDate

object ScheduleManagerFactory {

    fun getShiftManagerForDate(
        date: LocalDate,
    ): ScheduleManager<out Shift> = if (date >= ScheduleManager3.startDate) {
        ScheduleManager3
    } else if (date >= ScheduleManager2.startDate) {
        ScheduleManager2
    } else {
        ScheduleManager1
    }
}