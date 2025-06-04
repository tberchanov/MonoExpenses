package com.monoexpenses.presentation.categories.configuration.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.monoexpenses.presentation.categories.configuration.CategoriesConfigurationViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CategoriesConfigurationScreen(
    onBackClicked: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFF4F9F9)),
    ) {
        Header(14.dp, onBackClicked = onBackClicked)

        val viewModel = koinViewModel<CategoriesConfigurationViewModel>()
        val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    }
}
