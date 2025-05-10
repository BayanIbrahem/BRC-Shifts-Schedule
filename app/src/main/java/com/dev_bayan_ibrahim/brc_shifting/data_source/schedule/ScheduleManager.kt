package com.dev_bayan_ibrahim.brc_shifting.data_source.schedule

import com.dev_bayan_ibrahim.brc_shifting.util.Shift
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup
import kotlinx.datetime.LocalDate

interface ScheduleManager<S : Shift> {
    fun getShift(group: WorkGroup, date: LocalDate): S
    fun supportGroup(group: WorkGroup): Boolean
}