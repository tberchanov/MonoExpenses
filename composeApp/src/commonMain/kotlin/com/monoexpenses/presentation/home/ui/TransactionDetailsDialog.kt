package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.monoexpenses.domain.model.TransactionFullData
import com.monoexpenses.presentation.ui.theme.AppColors
import com.monoexpenses.utils.formatEpochSecondsToTimeDayMonthWeek
import com.monoexpenses.utils.formatMoney
import com.monoexpenses.utils.setText

@Composable
fun TransactionDetailsDialog(
    transactionData: TransactionFullData,
    onCloseDialog: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onCloseDialog() },
        title = {
            Text(
                "Transaction Details",
                style = MaterialTheme.typography.h6,
                color = AppColors.Primary
            )
        },
        text = {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val clipboardManager = LocalClipboardManager.current
                val userName = transactionData.userData.name ?: "Unknown"
                DetailRow("User Name", userName) {
                    clipboardManager.setText(userName)
                }
                DetailRow("Account Name", transactionData.account.name) {
                    clipboardManager.setText(transactionData.account.name)
                }
                transactionData.account.maskedPan?.let { maskedPanList ->
                    val maskedPan = maskedPanList.joinToString(", ")
                    DetailRow("Masked PAN", maskedPan) {
                        clipboardManager.setText(maskedPan)
                    }
                }
                val formattedDate =
                    remember { formatEpochSecondsToTimeDayMonthWeek(transactionData.transaction.time) }
                DetailRow("Date", formattedDate) {
                    clipboardManager.setText(formattedDate)
                }
                val formattedAmount =
                    remember(transactionData.transaction.amount) { formatMoney(transactionData.transaction.amount) }
                DetailRow("Amount", formattedAmount) {
                    clipboardManager.setText(formattedAmount)
                }
                DetailRow("Description", transactionData.transaction.description) {
                    clipboardManager.setText(transactionData.transaction.description)
                }
                DetailRow("MCC", transactionData.transaction.mcc.toString()) {
                    clipboardManager.setText(transactionData.transaction.mcc.toString())
                }
                val uriHandler = LocalUriHandler.current
                val mccUrl = "https://mcc.in.ua/"
                DetailRow("", mccUrl, true) {
                    uriHandler.openUri(mccUrl)
                }
                transactionData.transaction.receiptId?.let { receiptId ->
                    DetailRow("Receipt ID", receiptId) {
                        clipboardManager.setText(receiptId)
                    }
                }
                val checkUrl = "https://check.gov.ua/"
                DetailRow("", checkUrl, true) {
                    uriHandler.openUri(checkUrl)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onCloseDialog() }
            ) {
                Text("Close")
            }
        }
    )
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    isLink: Boolean = false,
    onValueClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            color = AppColors.Primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            textDecoration = if (isLink) TextDecoration.Underline else TextDecoration.None,
            color = if (isLink) AppColors.Primary else MaterialTheme.colors.onSurface,
            modifier = Modifier.clickable(onClick = onValueClick)
        )
    }
}
