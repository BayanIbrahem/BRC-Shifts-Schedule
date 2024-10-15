package com.dev_bayan_ibrahim.brc_shifting.util

import androidx.annotation.StringRes
import com.dev_bayan_ibrahim.brc_shifting.R

//enum class Shift(@StringRes val nameRes: Int) {
//    MORNING(R.string.shift_morning),
//    EVENING(R.string.shift_evening),
//    OFF_1(R.string.shift_off1),
//    OFF_2(R.string.shift_off2),
//}

/**
 * @property hourlyRange this is the start and the end hour of the shift during a cycle
 */
interface Shift {
    @get:StringRes
    val shiftName: Int
    val index: Int
    val cycleLength: Int get() = 96 // 4 days
}

enum class Shift1(
    @StringRes override val shiftName: Int,
) : Shift {
    // TODO, set the oldest shift schedule
    MORNING(R.string.shift_morning),
    EVENING(R.string.shift_evening),
    OFF_1(R.string.shift_off1),
    OFF_2(R.string.shift_off2);

    override val index = ordinal
}

enum class Shift2(
    @StringRes override val shiftName: Int,
) : Shift {
    MORNING(R.string.shift_morning),
    EVENING(R.string.shift_evening),
    OFF_1(R.string.shift_off1),
    OFF_2(R.string.shift_off2);

    override val index = ordinal
}

enum class Shift3(
    @StringRes override val shiftName: Int,
) : Shift {
    // TODO, set resources
    WORK(R.string.shift_work),
    REST1(R.string.shift_rest1),
    REST2(R.string.shift_rest2),
    REST3(R.string.shift_rest3);

    override val index = ordinal
}