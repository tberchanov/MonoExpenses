package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.monoexpenses.domain.model.CategorizedTransactions
import com.monoexpenses.presentation.ui.expandable.column.ExpandableItem
import com.monoexpenses.presentation.ui.expandable.column.ExpandableLazyColumn
import com.monoexpenses.presentation.ui.theme.AppColors

@Composable
fun Categories(
    modifier: Modifier = Modifier,
    categorizedTransactions: List<CategorizedTransactions>,
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
                .background(AppColors.ListHeaderColor),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Categories",
                modifier = Modifier.padding(horizontal = 12.dp),
                fontWeight = FontWeight.Bold,
            )
            // TODO implement categories configuration
            /*IconButton(
                onClick = {
//                onAddBankAccountsClick()
                },
            ) {
                Icon(painterResource(Res.drawable.settings), null, tint = Color.Black)
            }*/
        }
        Divider()
        ExpandableLazyColumn(
            expandableItems = categorizedTransactions.map {
                ExpandableItem(
                    it,
                    it.category.id,
                )
            },
            collapsedItemContent = { CategoryItem(it.data, it.isExpanded) },
            expandedItemContent = { CategoryTransactions(it.data) },
        )
    }
}

@Composable
private fun CategoryTransactions(categorizedTransactions: CategorizedTransactions) {
    Column {
        TransactionsHeader()
        categorizedTransactions.transactions.forEach { transaction ->
            Row {
                TransactionItem(transaction)
            }
        }
    }
}
