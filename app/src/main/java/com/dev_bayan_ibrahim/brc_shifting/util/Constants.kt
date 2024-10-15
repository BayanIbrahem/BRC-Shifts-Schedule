package com.dev_bayan_ibrahim.brc_shifting.util

import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup.A
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup.B
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup.C
import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup.D


val WorkGroup.shiftOn_Dec_12_2023: Shift
    get() = when (this) {
        A -> Shift2.OFF_1
        B -> Shift2.OFF_2
        C -> Shift2.MORNING
        D -> Shift2.EVENING
    }
const val referenceDateMillis: Long = 1_702_339_200_000L