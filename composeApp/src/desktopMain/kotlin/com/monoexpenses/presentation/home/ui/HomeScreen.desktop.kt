package com.monoexpenses.presentation.home.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.monoexpenses.domain.model.CategorizationData
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.Transaction
import com.monoexpenses.utils.formatMoney

@Composable
actual fun HomeData(
    categorizationData: CategorizationData,
    categories: List<Category>,
    onMoveToCategoryClicked: (Transaction) -> Unit,
    onCategoriesSettingsClicked: () -> Unit,
) {
    Row {
        UncategorizedExpenses(
            modifier = Modifier.weight(1f)
                .padding(HOME_CONTENT_PADDING),
            total = remember(categorizationData.uncategorizedTotalExpenses) {
                formatMoney(
                    categorizationData.uncategorizedTotalExpenses
                )
            },
            transactions = categorizationData.uncategorizedTransactions,
            onMoveToCategoryClicked = onMoveToCategoryClicked
        )
        Categories(
            modifier = Modifier.weight(1f).padding(HOME_CONTENT_PADDING),
            categorizedTransactions = categorizationData.categorizedTransactions,
            onCategoriesSettingsClicked = onCategoriesSettingsClicked,
        )
    }
}
