package com.dev_bayan_ibrahim.brc_shifting.ui.screen.deduction

data class DeductionsLogicActions(
    val onRefresh: () -> Unit,
    val onSelectMonth: (Int?) -> Unit,
    val onSelectyear: (Int?) -> Unit,
    val onResetDate: () -> Unit,
)
