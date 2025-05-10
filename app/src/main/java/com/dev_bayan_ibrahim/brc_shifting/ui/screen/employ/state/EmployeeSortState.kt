package com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.state

import androidx.annotation.StringRes
import com.dev_bayan_ibrahim.brc_shifting.R

data class EmployeeSortState(
    val sortBy: EmployeeSortBy = EmployeeSortBy.NAME,
    val asc: Boolean = true,
)

enum class EmployeeSortBy(@StringRes val nameRes: Int) {
    NAME(R.string.employee_sort_name),
    NUMBER(R.string.employee_sort_number),
    GROUP(R.string.employee_sort_group),
    CREATE_DATE(R.string.employee_sort_create),
    UPDATE_DATE(R.string.employee_sort_update),
}