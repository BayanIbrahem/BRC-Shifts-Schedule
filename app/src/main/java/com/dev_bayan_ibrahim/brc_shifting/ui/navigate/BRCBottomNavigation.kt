package com.dev_bayan_ibrahim.brc_shifting.ui.navigate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.bayan_ibrahim.my_dictionary.ui.navigate.topLevelNavigate

@Composable
fun BRCBottomNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Row(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(56.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BRCDestination.TopLevel.Enum.entries.forEach { topLevelRoute ->
            val selected by remember(currentDestination?.hierarchy) {
                derivedStateOf {
                    currentDestination?.hierarchy?.any {
                        it.hasRoute(topLevelRoute.route::class)
                    } == true
                }
            }
            NavigationBarItem(
                selected = selected,
                alwaysShowLabel = false,
                icon = {
                    TopLevelIcon(topLevelRoute)
                },
                label = {
                    Text(stringResource(topLevelRoute.labelRes))
                },
                onClick = {
                    navController.topLevelNavigate(topLevelRoute.route)
                }
            )
        }
    }
}

@Composable
private fun TopLevelIcon(
    route: BRCDestination.TopLevel.Enum,
    modifier: Modifier = Modifier,
) {
    when (route) {
        BRCDestination.TopLevel.Enum.Employees -> Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = modifier,
        )

        BRCDestination.TopLevel.Enum.Schedule -> Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            modifier = modifier,
        )

        BRCDestination.TopLevel.Enum.About -> Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = modifier,
        )
    }
}