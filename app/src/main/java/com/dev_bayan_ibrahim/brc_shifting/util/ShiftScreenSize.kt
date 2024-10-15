package com.dev_bayan_ibrahim.brc_shifting.util

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.dev_bayan_ibrahim.brc_shifting.util.ShiftScreenSize.*

enum class ShiftScreenSize {
    COMPAT, EXPANDED, SHORT,
}

fun WindowSizeClass.asShiftScreenSize(): ShiftScreenSize {
    if (heightSizeClass == WindowHeightSizeClass.Compact) {
        return SHORT
    }
    return when (widthSizeClass) {
        WindowWidthSizeClass.Expanded -> EXPANDED
        WindowWidthSizeClass.Medium -> EXPANDED
        WindowWidthSizeClass.Compact -> COMPAT
        else -> COMPAT
    }
}