package com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.cyclic

import com.dev_bayan_ibrahim.brc_shifting.util.CyclicShift3
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.datetime.LocalDate

object CyclicShiftScheduleManager3 : CyclicShiftScheduleManager<CyclicShift3> {
    override val startDate: LocalDate = LocalDate.parse("2024-10-15")
    override val lastValidDate: LocalDate = LocalDate.parse("2080-01-01")
    override val referenceDate: LocalDate = LocalDate.parse("2024-10-15")
    override val referencePoints: Map<WorkGroup.Cyclic, CyclicShift3> = WorkGroup.Cyclic.entries.associateWith {
        when (it) {
            WorkGroup.Cyclic.A -> CyclicShift3.REST2
            WorkGroup.Cyclic.B -> CyclicShift3.REST1
            WorkGroup.Cyclic.C -> CyclicShift3.WORK
            WorkGroup.Cyclic.D -> CyclicShift3.REST3
        }
    }

    override fun getShiftOfCycleIndex(
        index: Int,
        group: WorkGroup,
    ): CyclicShift3 = CyclicShift3.entries[index % CyclicShift3.entries.count()]
}