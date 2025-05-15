package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monoexpenses.domain.model.Transaction
import com.monoexpenses.utils.formatEpochSecondsToDayMonth
import com.monoexpenses.utils.formatMoney

@Composable
fun TransactionItem(transaction: Transaction) {
    var showDialog by remember { mutableStateOf(false) }

    Column(Modifier.clickable {
        showDialog = true
    }) {
        Divider()
        Row(Modifier.padding(4.dp)) {
            Text(formatEpochSecondsToDayMonth(transaction.time), modifier = Modifier.weight(1f))
            Text(
                remember(transaction.amount) { formatMoney(transaction.amount) },
                modifier = Modifier.weight(1f),
            )
            Text(transaction.description, modifier = Modifier.weight(2f))
        }
    }

    if (showDialog) {
        TransactionDetailsDialog(
            transaction,
            onCloseDialog = {
                showDialog = false
            },
        )
    }
}
