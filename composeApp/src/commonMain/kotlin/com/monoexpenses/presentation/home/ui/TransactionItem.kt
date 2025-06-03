package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monoexpenses.domain.model.Transaction
import com.monoexpenses.utils.formatEpochSecondsToDayMonth
import com.monoexpenses.utils.formatMoney

@Composable
fun TransactionItem(
    transaction: Transaction,
    showContextMenu: Boolean = false,
    onMoveToCategoryClicked: () -> Unit = {},
) {
    var showDialog by remember { mutableStateOf(false) }
    var showDropDown by remember { mutableStateOf(false) }

    Column(
        Modifier.combinedClickable(
            onClick = { showDialog = true },
            onLongClick = {
                if (showContextMenu) {
                    showDropDown = true
                }
            },
        )
    ) {
        Divider()
        Row(Modifier.padding(4.dp)) {
            Text(formatEpochSecondsToDayMonth(transaction.time), modifier = Modifier.weight(1f))
            Text(
                remember(transaction.amount) { formatMoney(transaction.amount) },
                modifier = Modifier.weight(1f),
            )
            Text(transaction.description, modifier = Modifier.weight(2f))
        }

        if (showDropDown) {
            DropdownMenu(
                expanded = showDropDown,
                onDismissRequest = { showDropDown = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Move to category") },
                    onClick = {
                        showDropDown = false
                        onMoveToCategoryClicked()
                    },
                )
            }
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
