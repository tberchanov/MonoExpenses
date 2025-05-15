package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.monoexpenses.domain.model.CategorizationData
import com.monoexpenses.presentation.add.accounts.ui.AddAccountsDialog
import com.monoexpenses.presentation.home.HomeViewModel
import com.monoexpenses.presentation.ui.theme.AppColors
import com.monoexpenses.utils.formatMoney
import org.koin.compose.viewmodel.koinViewModel

private val CONTENT_PADDING = 14.dp

@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeViewModel>()

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFF4F9F9)),
    ) {
        Header(
            padding = CONTENT_PADDING,
            onLoadClick = {
                viewModel.loadData()
            },
            onAddBankAccountsClick = {
                viewModel.displayAddBankAccount()
            },
        )

        val state by viewModel.stateFlow.collectAsStateWithLifecycle()

        state.apply {
            if (categorizationData != null) {
                HomeData(categorizationData)
            }
            if (loading != null) {
                HomeLoading(loading)
            }
            if (errorMessage != null) {
                HomeError(
                    errorMessage,
                    onDismiss = {
                        viewModel.resetError()
                    }
                )
            }
            if (showAddBankAccount) {
                HomeAddBankAccount(
                    onDismiss = {
                        viewModel.dismissAddBankAccount()
                    }
                )
            }
        }
    }
}

@Composable
fun HomeAddBankAccount(
    onDismiss: () -> Unit
) {
    AddAccountsDialog(
        onDismiss = onDismiss,
    )
}

@Composable
fun HomeData(categorizationData: CategorizationData) {
    Row {
        UncategorizedExpenses(
            modifier = Modifier.weight(1f)
                .padding(CONTENT_PADDING),
            total = remember(categorizationData.uncategorizedTotalExpenses) {
                formatMoney(
                    categorizationData.uncategorizedTotalExpenses
                )
            },
            transactions = categorizationData.uncategorizedTransactions,
        )
        Categories(
            modifier = Modifier.weight(1f).padding(CONTENT_PADDING),
            categorizedTransactions = categorizationData.categorizedTransactions,
        )
    }
}

@Composable
fun HomeLoading(showLoader: Boolean) {
    if (showLoader) {
        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.padding(CONTENT_PADDING),
                color = AppColors.Primary,
            )
        }
    }
}

@Composable
fun HomeError(errorMessage: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Error") },
        text = { Text(errorMessage) },
        confirmButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text("OK")
            }
        }
    )
}