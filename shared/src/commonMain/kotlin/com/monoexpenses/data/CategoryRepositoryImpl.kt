package com.monoexpenses.data

import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.CategoryFilter
import com.monoexpenses.domain.repository.CategoryRepository

internal class CategoryRepositoryImpl : CategoryRepository {
    override fun getCategories(): List<Category> {
        return listOf(
            Category(
                id = "1",
                name = "Food",
                categoryFilters = listOf(
                    CategoryFilter(id = "1", transactionMcc = 5499),
                    CategoryFilter(id = "2", transactionMcc = 5814),
                    CategoryFilter(id = "4", transactionMcc = 5812),
                )
            ),
            Category(
                id = "2",
                name = "Car",
                categoryFilters = listOf(
                    CategoryFilter(id = "3", transactionMcc = 7523),
                    CategoryFilter(id = "8", transactionMcc = 5541, transactionDescription = "WOG"),
                )
            ),
            Category(
                id = "3",
                name = "Subscriptions",
                categoryFilters = listOf(
                    CategoryFilter(
                        id = "5",
                        transactionMcc = 5734,
                        transactionDescription = "OpenAI",
                    ),
                    CategoryFilter(
                        id = "6",
                        transactionMcc = 8398,
                        transactionDescription = "WFP.CHARITY",
                        transactionAmount = -20400,
                    ),
                    CategoryFilter(
                        id = "6",
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
                    CategoryFilter(id = "7", transactionDescription = "Оксана Парикмахер"),
                )
            ),
        )
    }
}
