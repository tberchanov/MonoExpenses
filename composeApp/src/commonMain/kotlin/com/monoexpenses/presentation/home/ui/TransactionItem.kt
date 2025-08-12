package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.monoexpenses.domain.model.TransactionFullData
import com.monoexpenses.presentation.ui.theme.AppColors
import com.monoexpenses.utils.formatEpochSecondsToDayMonth
import com.monoexpenses.utils.formatMoney

@Composable
fun TransactionItem(
    transactionData: TransactionFullData,
    showContextMenu: Boolean = false,
    showCheckBox: Boolean = false,
    isChecked: Boolean = false,
    onMoveToCategoryClicked: () -> Unit = {},
    onSelectClicked: (Boolean) -> Unit = {},
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
            if (showCheckBox) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        onSelectClicked(it)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = AppColors.Primary
                    )
                )
            }
            Text(
                formatEpochSecondsToDayMonth(transactionData.transaction.time),
                modifier = Modifier.weight(1f),
            )
            Text(
                remember(transactionData.transaction.amount) { formatMoney(transactionData.transaction.amount) },
                modifier = Modifier.weight(1f),
            )
            Text(transactionData.transaction.description, modifier = Modifier.weight(2f))
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
                if (!showCheckBox) {
                    DropdownMenuItem(
                        text = { Text("Select") },
                        onClick = {
                            showDropDown = false
                            onSelectClicked(true)
                        },
                    )
                }
            }
        }
    }

    if (showDialog) {
        TransactionDetailsDialog(
            transactionData,
            onCloseDialog = {
                showDialog = false
            },
        )
    }
}
