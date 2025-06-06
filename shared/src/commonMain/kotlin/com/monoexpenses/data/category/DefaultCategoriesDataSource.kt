package com.monoexpenses.data.category

import co.touchlab.kermit.Logger
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.CategoryFilter

private const val TAG = "DefaultCategoriesDataSource"

internal class DefaultCategoriesDataSource {
    fun getDefaultCategories(): List<Category> {
        Logger.d(TAG) { "getCategories" }

        return listOf(
            Category(
                id = "1",
                name = "Food",
                categoryFilters = listOf(
                    CategoryFilter(transactionMcc = 5499),
                    CategoryFilter(transactionMcc = 5814),
                    CategoryFilter(transactionMcc = 5812),
                    CategoryFilter(transactionMcc = 5411),
                    CategoryFilter(transactionMcc = 5462),
                )
            ),
            Category(
                id = "2",
                name = "Car",
                categoryFilters = listOf(
                    CategoryFilter(transactionMcc = 7523),
                    CategoryFilter(transactionMcc = 5541),
                )
            ),
            Category(
                id = "3",
                name = "Subscriptions",
                categoryFilters = listOf(
                    CategoryFilter(
                        transactionMcc = 5734,
                        transactionDescription = "OpenAI",
                    ),
                    CategoryFilter(
                        transactionMcc = 8398,
                        transactionDescription = "WFP.CHARITY",
                        transactionAmount = -20400,
                    ),
                    CategoryFilter(
                        transactionMcc = 5815,
                        transactionDescription = "Patreon",
                        transactionAmount = -5020,
                    ),
                )
            ),
            Category(
                id = "4",
                name = "Health and Beauty",
                categoryFilters = listOf(
                    CategoryFilter(transactionMcc = 5912),
                )
            ),
            Category(
                id = "5",
                name = "Other",
                categoryFilters = listOf(

                )
            ),
            Category(
                id = "6",
                name = "Leisure",
                categoryFilters = listOf(
                    CategoryFilter(transactionMcc = 5942),
                )
            ),
            Category(
                id = "7",
                name = "Transport",
                categoryFilters = listOf(
                    CategoryFilter(transactionMcc = 4111)
                )
            ),
            Category(
                id = "8",
                name = "Communication services",
                categoryFilters = listOf(
                    CategoryFilter(transactionMcc = 4814)
                )
            ),
            Category(
                id = "9",
                name = "Presents",
                categoryFilters = listOf(
                    CategoryFilter(transactionMcc = 5992)
                )
            ),
        )
    }
}