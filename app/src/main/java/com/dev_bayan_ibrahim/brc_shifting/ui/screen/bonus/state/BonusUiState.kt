package com.dev_bayan_ibrahim.brc_shifting.ui.screen.bonus.state

import com.dev_bayan_ibrahim.brc_shifting.domain.model.Bonus
import com.dev_bayan_ibrahim.brc_shifting.domain.model.BonusType
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Employee

data class BonusUiState(
    val employee: Employee? = null,
    val acsOrder: Boolean = false,
    val isLoading: Boolean = false,
    val requestResult: Result<List<Bonus>>? = null,
    val selectedBonus: Set<Long> = emptySet(),
    val selectedBonusTypes: Set<BonusType> = emptySet(),
) {
    val isSelectionMode: Boolean get() = selectedBonus.isNotEmpty()
}
