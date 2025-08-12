package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.Transaction
import com.monoexpenses.domain.model.TransactionFullData
import com.monoexpenses.presentation.ui.theme.AppColors
import com.monoexpenses.utils.formatMoney

@Composable
fun MoveTransactionToCategoryDialog(
    transactionData: TransactionFullData,
    categories: List<Category>,
    onDismiss: () -> Unit,
    onMove: (Category) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Move Transaction",
                style = MaterialTheme.typography.h6,
                color = AppColors.Primary
            )
        },
        text = {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                TransactionSummary(transactionData.transaction)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Select Category",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            onClick = {
                                onMove(category)
                                onDismiss()
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun TransactionSummary(transaction: Transaction) {
    Column {
        Text(
            text = transaction.description,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = formatMoney(transaction.amount),
            style = MaterialTheme.typography.body1,
            color = AppColors.Primary
        )
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = category.name,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.weight(1f)
        )
    }
}