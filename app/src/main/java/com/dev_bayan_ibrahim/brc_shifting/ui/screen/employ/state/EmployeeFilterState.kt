package com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.state

import com.dev_bayan_ibrahim.brc_shifting.util.WorkGroup

data class EmployeeFilterState(
    val query: String = "",
    val groups: Set<WorkGroup> = emptySet(),
)