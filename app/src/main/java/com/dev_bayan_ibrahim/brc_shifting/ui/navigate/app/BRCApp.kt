package com.dev_bayan_ibrahim.brc_shifting.ui.navigate.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.dev_bayan_ibrahim.brc_shifting.ui.navigate.BRCBottomNavigationBar
import com.dev_bayan_ibrahim.brc_shifting.ui.navigate.BRCNavHost
import com.dev_bayan_ibrahim.brc_shifting.ui.navigate.allowDrawerGestures
import com.dev_bayan_ibrahim.brc_shifting.ui.navigate.getCurrentDestination
import com.dev_bayan_ibrahim.brc_shifting.ui.navigate.isTopLevel
import kotlinx.coroutines.launch

@Composable
fun BRCApp(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()

    val navBackState by navController.currentBackStackEntryFlow.collectAsStateWithLifecycle(initialValue = null)
    val currentDestination by remember(navBackState) {
        derivedStateOf {
            navBackState?.destination?.getCurrentDestination()
        }
    }
    val showBottomBar by remember(currentDestination) {
        derivedStateOf {
            currentDestination?.isTopLevel() == true
        }
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val gesturesEnabled by remember(currentDestination) {
        derivedStateOf {
            currentDestination?.allowDrawerGestures() != false
        }
    }
    LaunchedEffect(gesturesEnabled) {
        if (!gesturesEnabled) {
            drawerState.close()
        }
    }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = fadeIn() + expandVertically { it },
                exit = fadeOut() + shrinkVertically { it } // TODO, set animation
            ) {
                BRCBottomNavigationBar(navController = navController)
            }
        },
    ) { padding ->
        BackHandler(true) {
            if (drawerState.isOpen) {
                coroutineScope.launch {
                    drawerState.close()
                }
            } else {
                navController.popBackStack()
            }
        }
        BRCNavHost(
            navController = navController,
            modifier = Modifier.padding(padding).consumeWindowInsets(padding),
        )
    }
}