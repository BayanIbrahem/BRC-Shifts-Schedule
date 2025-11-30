package com.dev_bayan_ibrahim.brc_shifting.ui.screen.deduction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev_bayan_ibrahim.brc_shifting.ui.navigate.BRCDestination


@Composable
fun DeductionRoute(
    args: BRCDestination.Deductions,
    onPop: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DeductionsViewModel = hiltViewModel(),
) {
    DisposableEffect(args) {
        viewModel.initDeductions(args)
        onDispose { }
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val logicActions by remember {
        derivedStateOf {
            viewModel.buildLogicActions()
        }
    }
    val navActions by remember(onPop) {
        derivedStateOf {
            DeductionsNavActions(onPop)
        }
    }
    DeductionsScreen(
        state = uiState,
        actions = logicActions,
        navActions = navActions,
        modifier = modifier,
    )

}

