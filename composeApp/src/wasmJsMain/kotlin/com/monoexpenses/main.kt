package com.monoexpenses

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToNavigation
import com.monoexpenses.di.initKoin
import com.monoexpenses.presentation.ui.AppUI
import kotlinx.browser.document
import kotlinx.browser.window

@OptIn(ExperimentalComposeUiApi::class, ExperimentalBrowserHistoryApi::class)
fun main() {
    initKoin()
    ComposeViewport(document.body!!) {
        AppUI(onNavHostReady = {
            window.bindToNavigation(it)
        })
    }
}