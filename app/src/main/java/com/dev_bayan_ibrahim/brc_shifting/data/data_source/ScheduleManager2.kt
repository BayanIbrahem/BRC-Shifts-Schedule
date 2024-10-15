package com.dev_bayan_ibrahim.brc_shifting.data.data_source

import com.dev_bayan_ibrahim.brc_shifting.util.Shift2
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.datetime.LocalDate

object ScheduleManager2 : ScheduleManager<Shift2> {
    override val startDate: LocalDate = LocalDate.parse("2015-01-01") // TODO, set current start date
    override val lastValidDate: LocalDate = LocalDate.parse("2024-10-14")
    override val referenceDate: LocalDate = LocalDate.parse("2023-12-12")

    override val referencePoints: Map<WorkGroup, Shift2> = WorkGroup.entries.associateWith {
        when (it) {
            WorkGroup.A -> Shift2.OFF_1
            WorkGroup.B -> Shift2.OFF_2
            WorkGroup.C -> Shift2.MORNING
            WorkGroup.D -> Shift2.EVENING
        }
    }

    override fun getShiftOfCycleIndex(
        index: Int,
        group: WorkGroup,
    ): Shift2 = Shift2.entries[(index) % Shift2.entries.count()]
}

/**
 G          S
 0          2
 1          3
 2          0
 3          1
 */