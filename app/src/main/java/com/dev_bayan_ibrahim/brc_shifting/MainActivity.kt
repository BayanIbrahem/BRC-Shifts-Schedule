package com.dev_bayan_ibrahim.brc_shifting

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev_bayan_ibrahim.brc_shifting.ui.theme.BRCShiftsTheme
import com.dev_bayan_ibrahim.brc_shifting.util.asShiftScreenSize

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BRCShiftsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val windowSize = calculateWindowSizeClass(this)
                    Box (
                    ) {
                        ShiftApplication(
                            screenSize = windowSize.asShiftScreenSize()
                        )
                        val (versionName, applicationName) = LocalContext.current.run {
                            packageManager.getPackageInfo(packageName, 0).versionName to
                                    applicationInfo.loadLabel(packageManager).toString()
                        }
                        Text(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .alpha(0.75f)
                                .padding(8.dp),
                            text = getString(R.string.signeture, applicationName, versionName),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BRCShiftsTheme {
    }
}