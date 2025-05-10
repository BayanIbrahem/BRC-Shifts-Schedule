package com.dev_bayan_ibrahim.brc_shifting.util

import androidx.annotation.StringRes
import com.dev_bayan_ibrahim.brc_shifting.R

interface Shift {
    @get:StringRes
    val shiftName: Int
}

enum class StandardShift(
    @StringRes
    override val shiftName: Int,
) : Shift {
    WORK(R.string.shift_work),
    REST(R.string.shift_rest),
}

interface CyclicShift : Shift {
    val index: Int
    val cycleLength: Int get() = 96 // 4 days
}

enum class CyclicShift2(
    @StringRes override val shiftName: Int,
) : CyclicShift {
    MORNING(R.string.shift_morning),
    EVENING(R.string.shift_evening),
    OFF_1(R.string.shift_off1),
    OFF_2(R.string.shift_off2);

    override val index get() = ordinal
}

enum class CyclicShift3(
    @StringRes override val shiftName: Int,
) : CyclicShift {
    WORK(R.string.shift_work),
    REST1(R.string.shift_rest1),
    REST2(R.string.shift_rest2),
    REST3(R.string.shift_rest3);

    override val index get() = ordinal
}

///**
// * @property hourlyRange this is the start and the end hour of the shift during a cycle
// */
//interface CyclicShift {
//    @get:StringRes
//    val shiftName: Int
//    val index: Int
//    val cycleLength: Int get() = 96 // 4 days
//}
//
//enum class CyclicShift2(
//    @StringRes override val shiftName: Int,
//) : CyclicShift {
//    MORNING(R.string.shift_morning),
//    EVENING(R.string.shift_evening),
//    OFF_1(R.string.shift_off1),
//    OFF_2(R.string.shift_off2);
//
//    override val index get() = ordinal
//}
//
//enum class CyclicShift3(
//    @StringRes override val shiftName: Int,
//) : CyclicShift {
//    WORK(R.string.shift_work),
//    REST1(R.string.shift_rest1),
//    REST2(R.string.shift_rest2),
//    REST3(R.string.shift_rest3);
//
//    override val index get() = ordinal
//}