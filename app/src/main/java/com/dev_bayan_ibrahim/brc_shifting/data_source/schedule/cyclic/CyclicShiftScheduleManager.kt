package com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.cyclic

import com.dev_bayan_ibrahim.brc_shifting.data_source.schedule.ScheduleManager
import com.dev_bayan_ibrahim.brc_shifting.util.CyclicShift
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.datetime.LocalDate

interface CyclicShiftScheduleManager<S : CyclicShift> : CyclicShiftSchedule<S>, ScheduleManager<S> {
    /**
     * @param index from 0 to ([cycleDays] - 1)
     */
    fun getShiftOfCycleIndex(index: Int, group: WorkGroup): S
    override fun getShift(
        group: WorkGroup,
        date: LocalDate,
    ): S {
        require(group is WorkGroup.Cyclic)
        val daysDiff = (date.toEpochDays() - referenceDate.toEpochDays())
        val initialIndex = referencePoints[group]!!.index

        val cycleIndex = initialIndex
            .plus(daysDiff)
            .mod(cycleDays)
            .plus(cycleDays)
            .mod(cycleDays)
        return getShiftOfCycleIndex(cycleIndex, group)
    }

    override fun supportGroup(group: WorkGroup): Boolean = group is WorkGroup.Cyclic
}

