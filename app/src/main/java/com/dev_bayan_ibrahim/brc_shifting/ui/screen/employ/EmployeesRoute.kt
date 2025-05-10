package com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev_bayan_ibrahim.brc_shifting.ui.navigate.BRCDestination
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.actions.EmployeeCallback
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.actions.EmployeeNavActions

@Composable
fun EmployeesRoute(
    navigateToSalary: EmployeeCallback,
    navigateToBonus: EmployeeCallback,
    navigateToDayOffs: EmployeeCallback,
    navigateToDeduction: EmployeeCallback,
    modifier: Modifier = Modifier,
    viewModel: EmployViewModel = hiltViewModel(),
    args: BRCDestination.TopLevel.Employees,
) {
    val mutateState by viewModel.mutateState.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val sortState by viewModel.sortState.collectAsStateWithLifecycle()
    val allEmployees by viewModel.allEmployeesFlow.collectAsStateWithLifecycle()
    val scheduleOfToday by viewModel.scheduleOfToday.collectAsStateWithLifecycle()
    val logicActions by remember {
        derivedStateOf {
            viewModel.getLogicActions()
        }
    }
    val navActions by remember(
        navigateToSalary,
        navigateToBonus,
        navigateToDayOffs,
        navigateToDeduction,
    ) {
        derivedStateOf {
            EmployeeNavActions(
                navigateToSalary = navigateToSalary,
                navigateToBonus = navigateToBonus,
                navigateToDayOffs = navigateToDayOffs,
                navigateToDeduction = navigateToDeduction
            )
        }
    }
    EmployeeScreen(
        mutateState = mutateState,
        filterState = filterState,
        sortState = sortState,
        allEmployees = allEmployees,
        scheduleOfToday = scheduleOfToday,
        logicActions = logicActions,
        navActions = navActions,
        modifier = modifier,
    )
}
