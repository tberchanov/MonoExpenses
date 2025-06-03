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
                    CategoryFilter(transactionMcc = 5499),
                    CategoryFilter(transactionMcc = 5814),
                    CategoryFilter(transactionMcc = 5812),
                    CategoryFilter(transactionMcc = 5411),
                    CategoryFilter(transactionMcc = 5462),
                    CategoryFilter(transactionDescription = "Сільпо"),
                    CategoryFilter(transactionDescription = "TOV Parkovyy Keiteryng"),
                    CategoryFilter(transactionDescription = "TOV PARKOVYY KEiTERYNG"),
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
                    CategoryFilter(
                        transactionDescription = "УТ-2",
                    ),
                    CategoryFilter(
                        transactionDescription = "YouTube",
                    ),
                )
            ),
            Category(
                id = "4",
                name = "Health and Beauty",
                categoryFilters = listOf(
                    CategoryFilter(transactionDescription = "Оксана Парикмахер"),
                    CategoryFilter(transactionMcc = 5912),
                )
            ),
            Category(
                id = "5",
                name = "Other",
                categoryFilters = listOf(
                    CategoryFilter(transactionDescription = "З білої картки"),
                    CategoryFilter(transactionDescription = "Моя Прелесть"),
                    CategoryFilter(transactionDescription = "З гривневого рахунку ФОП"),
                    CategoryFilter(transactionDescription = "Мій Приват (універсальна)"),
                    CategoryFilter(transactionDescription = "Зарплата"),
                    CategoryFilter(transactionDescription = "З чорної картки"),
                    CategoryFilter(transactionDescription = "Від: Олександр Берчанов"),
                    CategoryFilter(transactionDescription = "Переказ на картку"),
                    CategoryFilter(transactionDescription = "Виведення кешбеку "),
                )
            ),
            Category(
                id = "6",
                name = "Leisure",
                categoryFilters = listOf(
                    CategoryFilter(transactionMcc = 5942),
                    CategoryFilter(transactionDescription = "MOODSHOPHarkiv"),
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
