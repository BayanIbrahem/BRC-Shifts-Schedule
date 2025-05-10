package com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.cyclic

import com.dev_bayan_ibrahim.brc_shifting.util.CyclicShift2
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.datetime.LocalDate

object CyclicShiftScheduleManager2 : CyclicShiftScheduleManager<CyclicShift2> {
    override val startDate: LocalDate = LocalDate.parse("1900-01-01") // TODO, set current start date
    override val lastValidDate: LocalDate = LocalDate.parse("2024-10-14")
    override val referenceDate: LocalDate = LocalDate.parse("2023-12-12")

    override val referencePoints: Map<WorkGroup.Cyclic, CyclicShift2> = WorkGroup.Cyclic.entries.associateWith {
        when (it) {
            WorkGroup.Cyclic.A -> CyclicShift2.OFF_1
            WorkGroup.Cyclic.B -> CyclicShift2.OFF_2
            WorkGroup.Cyclic.C -> CyclicShift2.MORNING
            WorkGroup.Cyclic.D -> CyclicShift2.EVENING
        }
    }

    override fun getShiftOfCycleIndex(
        index: Int,
        group: WorkGroup,
    ): CyclicShift2 = CyclicShift2.entries[(index) % CyclicShift2.entries.count()]
}