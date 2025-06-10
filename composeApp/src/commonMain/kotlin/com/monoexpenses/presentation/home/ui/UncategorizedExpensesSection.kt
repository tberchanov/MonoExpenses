package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.monoexpenses.domain.model.Transaction
import com.monoexpenses.presentation.ui.theme.AppColors
import expensesanalyzer.composeapp.generated.resources.Res
import expensesanalyzer.composeapp.generated.resources.cross
import org.jetbrains.compose.resources.painterResource

@Composable
fun UncategorizedExpenses(
    modifier: Modifier = Modifier,
    total: String,
    transactions: List<Transaction>,
    selectedTransactions: Set<Transaction>,
    onMoveToCategoryClicked: (Transaction) -> Unit,
    onSelectTransactionClicked: (Transaction, Boolean) -> Unit,
    onCloseSelection: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .shadow(2.dp)
            .border(DIVIDER_SIZE, AppColors.DividerColor)
            .fillMaxHeight(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AppColors.ListHeaderColor)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (selectedTransactions.isNotEmpty()) {
                Box(
                    Modifier
                        .padding(horizontal = 2.dp)
                        .padding(end = 2.dp)
                ) {
                    Icon(
                        painterResource(Res.drawable.cross),
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable(onClick = onCloseSelection)
                            .padding(2.dp),
                        contentDescription = "Close Selection",
                    )
                }
            }
            Text("Uncategorized Expenses", fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Text(total, fontWeight = FontWeight.Bold)
        }
        Divider()
        TransactionsHeader()
        val selectedTransactionsIds = remember(selectedTransactions) {
            selectedTransactions.map { it.id }.toSet()
        }
        VerticalList(
            modifier = Modifier.background(Color(0xFFF0F4F4))
                .fillMaxHeight(),
            items = transactions,
            key = { it.id },
        ) { transaction ->
            TransactionItem(
                transaction,
                showCheckBox = selectedTransactions.isNotEmpty(),
                isChecked = selectedTransactionsIds.contains(transaction.id),
                showContextMenu = true,
                onMoveToCategoryClicked = {
                    onMoveToCategoryClicked(transaction)
                },
                onSelectClicked = {
                    onSelectTransactionClicked(transaction, it)
                },
            )
        }
    }
}