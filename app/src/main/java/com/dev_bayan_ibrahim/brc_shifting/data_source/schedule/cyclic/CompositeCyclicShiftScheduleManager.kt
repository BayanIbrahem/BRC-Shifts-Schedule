package com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.cyclic

import com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.ScheduleManager
import com.dev_bayan_ibrahim.brc_shifting.util.CyclicShift
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.datetime.LocalDate

data class CompositeCyclicShiftScheduleManager(
    val managers: Set<CyclicShiftScheduleManager<out CyclicShift>> = setOf(
        CyclicShiftScheduleManager4,
        CyclicShiftScheduleManager3,
        CyclicShiftScheduleManager2,
    ),
) : ScheduleManager<CyclicShift> {
    override fun getShiftOrNull(
        group: WorkGroup,
        date: LocalDate,
    ): CyclicShift? {
        return managers.firstOrNull {
            it.startDate <= date
        }?.getShift(
            group = group,
            date = date
        )
    }

    override fun supportGroup(group: WorkGroup): Boolean = group is WorkGroup.Cyclic
}