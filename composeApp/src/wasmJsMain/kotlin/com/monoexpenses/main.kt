package com.monoexpenses

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.monoexpenses.di.initKoin
import com.monoexpenses.presentation.ui.AppUI
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    ComposeViewport(document.body!!) {
        AppUI()
    }
}