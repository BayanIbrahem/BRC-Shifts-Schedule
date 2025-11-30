package com.dev_bayan_ibrahim.brc_shifting.ui.screen.about

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dev_bayan_ibrahim.brc_shifting.ui.navigate.BRCDestination

@Composable
fun AboutRoute(
    args: BRCDestination.TopLevel.About,
    modifier: Modifier = Modifier,
) {
    AboutScreen(modifier = modifier)
}