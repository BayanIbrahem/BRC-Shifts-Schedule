package com.dev_bayan_ibrahim.brc_shifting.data.data_source

import androidx.compose.runtime.Stable
import com.dev_bayan_ibrahim.brc_shifting.util.Shift
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.datetime.LocalDate

@Stable
interface ShiftSchedule<S : Shift> {
    val startDate: LocalDate
    val lastValidDate: LocalDate
    val referenceDate: LocalDate
    val referencePoints: Map<WorkGroup, S>
    val cycleDays: Int get() = 4
}