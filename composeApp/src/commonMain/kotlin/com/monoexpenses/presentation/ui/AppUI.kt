package com.monoexpenses.presentation.ui

import androidx.compose.runtime.Composable
import com.monoexpenses.presentation.home.ui.HomeScreen
import com.monoexpenses.presentation.ui.theme.MonoExpensesTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun AppUI() {
    MonoExpensesTheme {
        HomeScreen()
    }
}