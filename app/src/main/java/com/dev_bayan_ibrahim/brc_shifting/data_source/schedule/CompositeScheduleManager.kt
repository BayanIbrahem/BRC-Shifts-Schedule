package com.dev_bayan_ibrahim.brc_shifting.data_source.schedule

import com.dev_bayan_ibrahim.brc_shifting.util.Shift
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.datetime.LocalDate

data class CompositeScheduleManager(
    val managers: Set<ScheduleManager<out Shift>>,
) : ScheduleManager<Shift> {
    override fun getShiftOrNull(group: WorkGroup, date: LocalDate): Shift? {
        return managers.firstOrNull {
            it.supportGroup(group)
        }?.getShift(
            group = group,
            date = date
        )
    }

    override fun supportGroup(group: WorkGroup): Boolean {
        return managers.any { it.supportGroup(group) }
    }
}