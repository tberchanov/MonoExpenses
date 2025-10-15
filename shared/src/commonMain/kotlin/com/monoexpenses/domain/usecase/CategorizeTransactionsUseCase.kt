package com.monoexpenses.domain.usecase

import co.touchlab.kermit.Logger
import com.monoexpenses.domain.model.CategorizationData
import com.monoexpenses.domain.model.CategorizedTransactions
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.CategoryFilter
import com.monoexpenses.domain.model.Transaction
import com.monoexpenses.domain.model.TransactionFullData

private const val TAG = "CategorizeTransactionsUseCase"
private const val REGEX_PREFIX = "\r"

class CategorizeTransactionsUseCase {

    fun execute(
        transactions: List<TransactionFullData>,
        categories: List<Category>
    ): CategorizationData {
        Logger.d(TAG) { "execute: ${transactions.size} ${categories.size}" }
        val uncategorizedTransactions = mutableListOf<TransactionFullData>()
        val categoriesMap = mutableMapOf<Category, MutableList<TransactionFullData>>()

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

        uncategorizedTransactions.sortBy { it.transaction.time }

        val categorizedTransactions = categoriesMap.map { (category, transactions) ->
            CategorizedTransactions(
                category,
                transactions,
                totalExpenses = calcTotalExpenses(transactions.map { it.transaction })
            )
        }.sortedBy { it.category.name.lowercase() }

        return CategorizationData(
            categorizedTransactions,
            uncategorizedTransactions,
            uncategorizedTotalExpenses = calcTotalExpenses(uncategorizedTransactions.map { it.transaction }),
        )
    }

    private fun calcTotalExpenses(transactions: List<Transaction>): Long {
        return transactions.sumOf { it.amount }
    }

    private fun getTransactionCategory(
        transactionData: TransactionFullData,
        categories: List<Category>,
    ): Category? {
        val matchedCategories = mutableListOf<Pair<Category, CategoryFilter>>()
        categories.forEach { category ->
            val matchedFilter =
                getTransactionMatchingFilter(transactionData.transaction, category.categoryFilters)
            if (matchedFilter != null) {
                matchedCategories.add(category to matchedFilter)
            }
        }

        if (matchedCategories.size > 1) {
            val matchedCategoriesStr =
                matchedCategories.joinToString { "{${it.first.id} ${it.first.name}: ${it.second}}" }
            throw IllegalStateException("Transaction is matched for a few categories. ${transactionData.transaction} -> $matchedCategoriesStr")
        }
        return matchedCategories.firstOrNull()?.first
    }

    private fun getTransactionMatchingFilter(
        transaction: Transaction,
        filters: List<CategoryFilter>,
    ): CategoryFilter? {
        val matchedFilter = filters.firstOrNull { filter ->
            isTransactionMatchingFilter(transaction, filter)
        }
        return matchedFilter
    }

    private fun isTransactionMatchingFilter(
        transaction: Transaction,
        filter: CategoryFilter,
    ): Boolean {
        val mccMatching = filter.transactionMcc?.equals(transaction.mcc) ?: true
        val amountMatching = filter.transactionAmount?.equals(transaction.amount) ?: true
        val isDescriptionRegex = filter.transactionDescription?.startsWith(REGEX_PREFIX) ?: false
        val descriptionMatching =
            if (isDescriptionRegex) {
                try {
                    Regex(filter.transactionDescription).matches(transaction.description)
                } catch (e: Exception) {
                    Logger.e(TAG) { "Invalid regex pattern: ${filter.transactionDescription}" }
                    transaction.description == filter.transactionDescription
                }
            } else {
                transaction.description == filter.transactionDescription
            }
        return mccMatching && amountMatching && descriptionMatching
    }
}
