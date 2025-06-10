package com.monoexpenses.presentation.accounts.configuration.ui

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
import com.monoexpenses.presentation.accounts.configuration.AccountsConfigurationViewModel
import com.monoexpenses.presentation.ui.ErrorDialog
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AccountsConfigurationScreen(
    onBackClicked: () -> Unit = {},
) {
    val viewModel = koinViewModel<AccountsConfigurationViewModel>()

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFF4F9F9)),
    ) {
        Header(
            14.dp,
            onBackClicked = onBackClicked,
        )

        val state by viewModel.stateFlow.collectAsStateWithLifecycle()
        state.apply {
            if (errorMessage != null) {
                ErrorDialog(
                    errorMessage,
                    onDismiss = {
                        viewModel.resetError()
                    }
                )
            }
            if (categories != null) {

            }
        }
    }
}

@Preview
@Composable
fun AccountsConfigurationScreenPreview() {
    AccountsConfigurationScreen()
}