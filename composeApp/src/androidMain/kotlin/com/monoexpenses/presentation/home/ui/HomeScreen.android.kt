package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.monoexpenses.domain.model.CategorizationData
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.Transaction

@Composable
actual fun HomeData(
    categorizationData: CategorizationData,
    categories: List<Category>,
    onMoveToCategoryClicked: (Transaction) -> Unit,
    onCategoriesSettingsClicked: () -> Unit,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Uncategorized", "Categories")

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> UncategorizedExpenses(
                modifier = Modifier.weight(1f),
                total = categorizationData.uncategorizedTotalExpenses.toString(),
                transactions = categorizationData.uncategorizedTransactions,
                onMoveToCategoryClicked = onMoveToCategoryClicked,
            )

            1 -> Categories(
                modifier = Modifier.weight(1f),
                categorizedTransactions = categorizationData.categorizedTransactions,
                onCategoriesSettingsClicked = onCategoriesSettingsClicked,
            )
        }
    }
}