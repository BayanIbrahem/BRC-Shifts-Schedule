package com.dev_bayan_ibrahim.brc_shifting.ui.screen.bonus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev_bayan_ibrahim.brc_shifting.ui.navigate.BRCDestination
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.bonus.action.BonusNavActions

@Composable
fun BonusRoute(
    args: BRCDestination.Bonus,
    onPop: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BonusViewModel = hiltViewModel()
) {
    DisposableEffect(args) {
        viewModel.initBonus(args)
        onDispose {  }
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val bonusList by viewModel.bonusList.collectAsStateWithLifecycle()
    val bonusTypes by viewModel.bonusTypeSumAndCount.collectAsStateWithLifecycle()
    val logicActions by remember {
        derivedStateOf {
            viewModel.buildLogicActions()
        }
    }
    val navActions by remember(onPop) {
        derivedStateOf {
            BonusNavActions(
                onPop = onPop
            )
        }
    }

    BonusScreen(
        uiState = uiState,
        bonusList = bonusList,
        bonusTypes = bonusTypes,
        logicActions = logicActions,
        navActions = navActions,
        modifier = modifier,
    )

}