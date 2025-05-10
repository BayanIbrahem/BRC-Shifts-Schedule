package com.dev_bayan_ibrahim.brc_shifting.ui.screen.bonus.action

import com.dev_bayan_ibrahim.brc_shifting.domain.model.Bonus
import com.dev_bayan_ibrahim.brc_shifting.domain.model.BonusType

data class BonusLogicActions(
    val onFetch: () -> Unit,
    val onDeleteBonus: (Bonus) -> Unit,
    val onToggleSelectBonus: (Bonus) -> Unit,
    val onToggleSelectBonusType: (BonusType) -> Unit,
    val onToggleOrder: (asc: Boolean)  -> Unit,
    val onCancelSelectBonus: () -> Unit,
    val onSetSelectedBonuses: (bonusList: List<Bonus>) -> Unit,
    val onDeleteSelectedBonuses: () -> Unit,
    val onDeleteAllBonuses: () -> Unit,
)