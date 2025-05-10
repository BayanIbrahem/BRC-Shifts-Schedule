package com.dev_bayan_ibrahim.brc_shifting.ui.screen.shedule

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev_bayan_ibrahim.brc_shifting.ui.navigate.BRCDestination


@Composable
fun ScheduleRoute(
    args: BRCDestination.TopLevel.Schedule,
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val shifts by viewModel.groupShifts.collectAsStateWithLifecycle()
    val logicActions by remember {
        derivedStateOf {
            viewModel.buildLogicActions()
        }
    }
    ScheduleScreen(modifier, uiState, viewModel, logicActions, shifts)
}




