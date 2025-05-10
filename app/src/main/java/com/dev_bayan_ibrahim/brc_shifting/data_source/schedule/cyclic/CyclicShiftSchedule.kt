package com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.cyclic

import androidx.compose.runtime.Stable
import com.dev_bayan_ibrahim.brc_shifting.util.CyclicShift
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.datetime.LocalDate

@Stable
interface CyclicShiftSchedule<S : CyclicShift> {
    val startDate: LocalDate
    val lastValidDate: LocalDate
    val referenceDate: LocalDate
    val referencePoints: Map<WorkGroup.Cyclic, S>
    val cycleDays: Int get() = 4
}