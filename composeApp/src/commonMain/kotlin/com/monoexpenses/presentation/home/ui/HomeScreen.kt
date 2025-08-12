package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.monoexpenses.domain.model.CategorizationData
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.TransactionFullData
import com.monoexpenses.presentation.home.HomeViewModel
import com.monoexpenses.presentation.home.ui.calendar.CalendarDialog
import com.monoexpenses.presentation.ui.ErrorDialog
import com.monoexpenses.presentation.ui.theme.AppColors
import org.koin.compose.viewmodel.koinViewModel

val HOME_CONTENT_PADDING = 14.dp

@Composable
fun HomeScreen(
    isCategoriesModified: Boolean = false,
    onCategoriesSettingsClicked: () -> Unit,
    onAddBankAccountsClicked: () -> Unit,
) {
    val viewModel = koinViewModel<HomeViewModel>()
    if (isCategoriesModified) {
        LaunchedEffect(isCategoriesModified) {
            viewModel.recategorizeTransactions()
        }
    }
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xFFF4F9F9)),
    ) {
        Header(
            padding = HOME_CONTENT_PADDING,
            onLoadClick = {
                viewModel.onLoadClick()
            },
            onAddBankAccountsClick = {
                onAddBankAccountsClicked()
            },
            selectedDateMessage = state.selectedDateMessage,
        )

        state.apply {
            if (loading != null) {
                HomeLoading(loading)
            }
            if (categorizationData != null) {
                var moveTransactionToCategory: TransactionFullData? by remember { mutableStateOf(null) }
                HomeData(
                    categorizationData,
                    selectedTransactions,
                    categories,
                    onMoveToCategoryClicked = { transaction ->
                        moveTransactionToCategory = transaction
                    },
                    onCategoriesSettingsClicked = onCategoriesSettingsClicked,
                    onSelectTransactionClicked = { transaction, isSelected ->
                        viewModel.selectTransaction(transaction, isSelected)
                    },
                    onCloseSelection = {
                        viewModel.closeSelection()
                    }
                )
                moveTransactionToCategory?.let { transaction ->
                    MoveTransactionToCategoryDialog(
                        transactionData = transaction,
                        categories = categories,
                        onDismiss = { moveTransactionToCategory = null },
                        onMove = { category ->
                            viewModel.moveTransactionToCategory(transaction, category)
                        }
                    )
                }
            }
            if (errorMessage != null) {
                ErrorDialog(
                    errorMessage,
                    onDismiss = {
                        viewModel.resetError()
                    }
                )
            }
            if (showCalendarDialog) {
                CalendarDialog(
                    onDismiss = {
                        viewModel.dismissCalendar()
                    },
                    onDateSelected = { startDate, endDate ->
                        viewModel.loadData(startDate, endDate)
                    },
                )
            }
        }
    }
}

@Composable
expect fun HomeData(
    categorizationData: CategorizationData,
    selectedTransactions: Set<TransactionFullData>,
    categories: List<Category>,
    onMoveToCategoryClicked: (TransactionFullData) -> Unit,
    onCategoriesSettingsClicked: () -> Unit,
    onSelectTransactionClicked: (TransactionFullData, Boolean) -> Unit,
    onCloseSelection: () -> Unit = {},
)

@Composable
fun HomeLoading(showLoader: Boolean) {
    if (showLoader) {
        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.padding(HOME_CONTENT_PADDING),
                color = AppColors.Primary,
            )
        }
    }
}
