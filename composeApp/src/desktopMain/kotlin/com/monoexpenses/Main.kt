package com.monoexpenses

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.monoexpenses.di.initKoin
import com.monoexpenses.presentation.ui.AppUI

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Expenses Analyzer",
        ) {
            AppUI()
        }
    }
}
