package com.dev_bayan_ibrahim.brc_shifting.util

import androidx.annotation.StringRes
import com.dev_bayan_ibrahim.brc_shifting.R

enum class WorkGroup(@StringRes val nameRes: Int) {
    A(R.string.group_name_a),
    B(R.string.group_name_b),
    C(R.string.group_name_c),
    D(R.string.group_name_d),
}
