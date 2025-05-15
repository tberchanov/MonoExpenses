package com.monoexpenses.presentation.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.monoexpenses.presentation.ui.theme.AppColors.Primary

object AppColors {
    val Primary = Color(0xFF0A3D62)
    val DividerColor = Color(0xFFc1c9c9)
    val ListHeaderColor = Color(0xFFE6ECEC)
    val AccentColor = Color(0xFF1E90A6)
}

private val LightColorPalette = lightColors(
    primary = Primary,
)

@Composable
fun MonoExpensesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        content = content
    )
} 