package com.dev_bayan_ibrahim.brc_shifting.ui.navigate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.bonus.BonusRoute
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.day_off.DayOffsRoute
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.deduction.DeductionRoute
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.employ.EmployeesRoute
import com.dev_bayan_ibrahim.brc_shifting.ui.screen.salary.SalariesRoute

@Composable
fun BRCNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = BRCDestination.TopLevel.Employees
    ) {
        composable<BRCDestination.TopLevel.Employees> { backStackEntry ->
            val employees: BRCDestination.TopLevel.Employees = backStackEntry.toRoute()
            EmployeesRoute(
                args = employees,
                navigateToSalary = {
                    navController.navigate(BRCDestination.Salaries(it.employeeNumber))
                },
                navigateToBonus = {
                    navController.navigate(BRCDestination.Bonus(it.employeeNumber))
                },
                navigateToDayOffs = {
                    navController.navigate(BRCDestination.DayOffs(it.employeeNumber))
                },
                navigateToDeduction = {
                    navController.navigate(BRCDestination.Deductions(it.employeeNumber))
                }
            )
        }

        composable<BRCDestination.Salaries> { backStackEntry ->
            val salaries: BRCDestination.Salaries = backStackEntry.toRoute()
            SalariesRoute(args = salaries, onPop = navController::popBackStack)
        }

        composable<BRCDestination.Bonus> { backStackEntry ->
            val bonus: BRCDestination.Bonus = backStackEntry.toRoute()
            BonusRoute(args = bonus, onPop = navController::popBackStack)
        }

        composable<BRCDestination.DayOffs> { backStackEntry ->
            val dayOffs: BRCDestination.DayOffs = backStackEntry.toRoute()
            DayOffsRoute(args = dayOffs)
        }

        composable<BRCDestination.Deductions> { backStackEntry ->
            val deductions: BRCDestination.Deductions = backStackEntry.toRoute()
            DeductionRoute(args = deductions)
        }
    }
}

