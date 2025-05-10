package dev.bayan_ibrahim.my_dictionary.ui.navigate

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.dev_bayan_ibrahim.brc_shifting.ui.navigate.BRCDestination

fun NavHostController.topLevelNavigate(route: BRCDestination.TopLevel) = navigate(route) {
    popUpTo(graph.findStartDestination().id) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}
