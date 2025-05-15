package com.monoexpenses

import androidx.compose.ui.window.ComposeUIViewController
import com.monoexpenses.di.initKoin
import com.monoexpenses.presentation.ui.AppUI

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    },
) {
    AppUI()
}