package com.dev_bayan_ibrahim.brc_shifting.data.data_source

import com.dev_bayan_ibrahim.brc_shifting.util.Shift1
import com.dev_bayan_ibrahim.brc_shifting.util.Shift2
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.datetime.LocalDate

object ScheduleManager1 : ScheduleManager<Shift1> {
    override val startDate: LocalDate = LocalDate.fromEpochDays(0) // is the first date ever
    override val lastValidDate: LocalDate = LocalDate.parse("2015-01-01")

    // TODO, set date for it
    override val referenceDate: LocalDate = LocalDate.parse("2023-12-12")

    override val referencePoints: Map<WorkGroup, Shift1> = WorkGroup.entries.associateWith {
        when (it) {
            WorkGroup.A -> Shift1.OFF_1
            WorkGroup.B -> Shift1.OFF_2
            WorkGroup.C -> Shift1.MORNING
            WorkGroup.D -> Shift1.EVENING
        }
    }

    override fun getShiftOfCycleIndex(
        index: Int,
        group: WorkGroup,
    ): Shift1 = Shift1.entries[index + group.ordinal % Shift2.entries.count()]
}