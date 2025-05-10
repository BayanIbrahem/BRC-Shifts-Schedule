package com.dev_bayan_ibrahim.brc_shifting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.dev_bayan_ibrahim.brc_shifting.ui.navigate.app.BRCApp
import com.dev_bayan_ibrahim.brc_shifting.ui.theme.BRCShiftsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BRCShiftsTheme {
                BRCApp()
            }
        }
    }
}
