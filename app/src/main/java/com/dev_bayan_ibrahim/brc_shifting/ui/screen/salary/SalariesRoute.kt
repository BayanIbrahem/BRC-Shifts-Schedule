package com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev_bayan_ibrahim.brc_shifting.ui.navigate.BRCDestination
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.action.SalaryNavActions

@Composable
fun SalariesRoute(
    args: BRCDestination.Salaries,
    onPop: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SalariesViewModel = hiltViewModel(),
) {
    DisposableEffect(args) {
        viewModel.initSalaries(args)
        onDispose { }
    }
    val salaries by viewModel.salaries.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val thisMonth by viewModel.thisMonthFlow.collectAsStateWithLifecycle()
    val logicActions by remember {
        derivedStateOf {
            viewModel.buildLogicActions()
        }
    }
    val navActions by remember(onPop) {
        derivedStateOf {
            SalaryNavActions(onPop)
        }
    }
    SalariesScreen(
        uiState,
        salaries,
        thisMonth,
        logicActions,
        navActions,
        modifier,
    )

}

