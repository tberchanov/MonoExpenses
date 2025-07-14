package com.monoexpenses.domain.usecase

import co.touchlab.kermit.Logger
import com.monoexpenses.domain.model.CategorizationData
import com.monoexpenses.domain.model.CategorizedTransactions
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.CategoryFilter
import com.monoexpenses.domain.model.Transaction

private const val TAG = "CategorizeTransactionsUseCase"

class CategorizeTransactionsUseCase {

    fun execute(transactions: List<Transaction>, categories: List<Category>): CategorizationData {
        Logger.d(TAG) { "execute: ${transactions.size} ${categories.size}" }
        val uncategorizedTransactions = mutableListOf<Transaction>()
        val categoriesMap = mutableMapOf<Category, MutableList<Transaction>>()

        transactions.forEach { transaction ->
            val category = getTransactionCategory(transaction, categories)
            if (category == null) {
                uncategorizedTransactions.add(transaction)
            } else {
                categoriesMap[category]
                    ?.apply { add(transaction) }
                    ?: run { categoriesMap[category] = mutableListOf(transaction) }
            }
        }

        uncategorizedTransactions.sortBy { it.time }

        val categorizedTransactions = categoriesMap.map { (category, transactions) ->
            CategorizedTransactions(
                category,
                transactions,
                totalExpenses = calcTotalExpenses(transactions)
            )
        }.sortedBy { it.category.name.lowercase() }

        return CategorizationData(
            categorizedTransactions,
            uncategorizedTransactions,
            uncategorizedTotalExpenses = calcTotalExpenses(uncategorizedTransactions),
        )
    }

    private fun calcTotalExpenses(transactions: List<Transaction>): Long {
        return transactions.sumOf { it.amount }
    }

    private fun getTransactionCategory(
        transaction: Transaction,
        categories: List<Category>,
    ): Category? {
        val matchedCategories = mutableListOf<Pair<Category, CategoryFilter>>()
        categories.forEach { category ->
            val matchedFilter = getTransactionMatchingFilter(transaction, category.categoryFilters)
            if (matchedFilter != null) {
                matchedCategories.add(category to matchedFilter)
            }
        }

        if (matchedCategories.size > 1) {
            val matchedCategoriesStr =
                matchedCategories.joinToString { "{${it.first.id} ${it.first.name}: ${it.second}}" }
            throw IllegalStateException("Transaction is matched for a few categories. $transaction -> $matchedCategoriesStr")
        }
        return matchedCategories.firstOrNull()?.first
    }

    private fun getTransactionMatchingFilter(
        transaction: Transaction,
        filters: List<CategoryFilter>,
    ): CategoryFilter? {
        val matchedFilter = filters.firstOrNull { filter ->
            val mccMatching = filter.transactionMcc?.equals(transaction.mcc) ?: true
            val amountMatching = filter.transactionAmount?.equals(transaction.amount) ?: true
            val descriptionMatching =
                filter.transactionDescription?.let {
                    try {
                        Regex(it).matches(transaction.description)
                    } catch (e: Exception) {
                        Logger.e(TAG) { "Invalid regex pattern: $it" }
                        transaction.description == it
                    }
                } ?: true
            mccMatching && amountMatching && descriptionMatching
        }
        return matchedFilter
    }
}