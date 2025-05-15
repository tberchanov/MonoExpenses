package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.monoexpenses.domain.model.Transaction
import com.monoexpenses.presentation.ui.theme.AppColors

@Composable
fun UncategorizedExpenses(
    modifier: Modifier = Modifier,
    total: String,
    transactions: List<Transaction>,
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
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Uncategorized Expenses", fontWeight = FontWeight.Bold)
            Text(total, fontWeight = FontWeight.Bold)
        }
        Divider()
        TransactionsHeader()
        LazyColumn(
            modifier = Modifier.background(Color(0xFFF0F4F4))
                .fillMaxHeight(),
        ) {
            items(
                items = transactions,
                key = { it.id },
            ) { transaction ->
                TransactionItem(transaction)
            }
        }
    }
}