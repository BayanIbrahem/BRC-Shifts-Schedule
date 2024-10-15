package com.dev_bayan_ibrahim.brc_shifting.data.data_source

import com.dev_bayan_ibrahim.brc_shifting.util.Shift2
import com.dev_bayan_ibrahim.brc_shifting.util.Shift3
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.datetime.LocalDate

object ScheduleManager3 : ScheduleManager<Shift3> {
    override val startDate: LocalDate = LocalDate.parse("2024-10-15")
    override val lastValidDate: LocalDate = LocalDate.parse("2080-01-01")
    override val referenceDate: LocalDate = LocalDate.parse("2024-10-15")
    override val referencePoints: Map<WorkGroup, Shift3> = WorkGroup.entries.associateWith {
        when (it) {
            WorkGroup.A -> Shift3.REST2
            WorkGroup.B -> Shift3.REST1
            WorkGroup.C -> Shift3.WORK
            WorkGroup.D -> Shift3.REST3
        }
    }

    override fun getShiftOfCycleIndex(
        index: Int,
        group: WorkGroup,
    ): Shift3 = Shift3.entries[(index + group.ordinal) % Shift2.entries.count()]
}