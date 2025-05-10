package com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.standard

import com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.ScheduleManager
import com.dev_bayan_ibrahim.brc_shifting.util.StandardShift
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

data object StandardScheduleManager : ScheduleManager<StandardShift> {
    override fun getShift(group: WorkGroup, date: LocalDate): StandardShift {
        require(group is WorkGroup.Standard)
        val isWeekend = isWeekend(date)
        return if (isWeekend) {
            StandardShift.REST
        } else {
            StandardShift.WORK
        }
    }

    override fun supportGroup(group: WorkGroup): Boolean = group is WorkGroup.Standard

    private fun isWeekend(date: LocalDate): Boolean {
        val dayOfWeek = date.dayOfWeek
        return dayOfWeek in setOf(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
    }
}