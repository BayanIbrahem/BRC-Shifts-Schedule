package com.dev_bayan_ibrahim.brc_shifting.data.data_source

import com.dev_bayan_ibrahim.brc_shifting.util.Shift
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

interface ScheduleManager<S : Shift> : ShiftSchedule<S> {
    /**
     * @param index from 0 to ([cycleDays] - 1)
     */
    fun getShiftOfCycleIndex(index: Int, group: WorkGroup): S
    fun calculateShiftForDate(
        date: LocalDate,
        group: WorkGroup,
    ): S {
        val daysDiff = (date.toEpochDays() - referenceDate.toEpochDays())
        val initialIndex = referencePoints[group]!!.index

        val cycleIndex = initialIndex
            .plus(daysDiff)
            .mod(cycleDays)
            .plus(cycleDays)
            .mod(cycleDays)
        return getShiftOfCycleIndex(cycleIndex, group)
    }
}
