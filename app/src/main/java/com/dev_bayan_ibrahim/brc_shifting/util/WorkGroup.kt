package com.dev_bayan_ibrahim.brc_shifting.util

import androidx.annotation.StringRes
import com.dev_bayan_ibrahim.brc_shifting.R

const val WORK_GROUP_KEY_A = "A"
const val WORK_GROUP_KEY_B = "B"
const val WORK_GROUP_KEY_C = "C"
const val WORK_GROUP_KEY_D = "D"
const val WORK_GROUP_KEY_STANDARD = "STANDARD"

sealed interface WorkGroup {
    @get:StringRes
    val nameRes: Int

    @get:StringRes
    val shortcutRes: Int
    val key: String
    val orderNumber: Int

    enum class Cyclic(
        @StringRes override val nameRes: Int,
        @StringRes override val shortcutRes: Int,
        override val key: String,
    ) : WorkGroup {
        A(nameRes = R.string.group_name_a, shortcutRes = R.string.group_shortcut_a, key = WORK_GROUP_KEY_A),
        B(nameRes = R.string.group_name_b, shortcutRes = R.string.group_shortcut_b, key = WORK_GROUP_KEY_B),
        C(nameRes = R.string.group_name_c, shortcutRes = R.string.group_shortcut_c, key = WORK_GROUP_KEY_C),
        D(nameRes = R.string.group_name_d, shortcutRes = R.string.group_shortcut_d, key = WORK_GROUP_KEY_D);

        override val orderNumber: Int
            get() = ordinal
    }

    data object Standard : WorkGroup {
        override val nameRes: Int
            get() = R.string.group_name_standard
        override val shortcutRes: Int
            get() = R.string.group_shortcut_standard
        override val key: String
            get() = WORK_GROUP_KEY_STANDARD
        override val orderNumber: Int
            get() = Cyclic.entries.count()
    }

    companion object {
        val entries: List<WorkGroup> = Cyclic.entries + Standard
        operator fun invoke(key: String): WorkGroup {
            return when (key) {
                WORK_GROUP_KEY_A -> Cyclic.A
                WORK_GROUP_KEY_B -> Cyclic.B
                WORK_GROUP_KEY_C -> Cyclic.C
                WORK_GROUP_KEY_D -> Cyclic.D
                else -> Standard
            }
        }
    }
}
