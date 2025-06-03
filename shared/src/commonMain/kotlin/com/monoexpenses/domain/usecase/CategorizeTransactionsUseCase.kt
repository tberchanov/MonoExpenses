package com.monoexpenses.domain.usecase

import com.monoexpenses.domain.model.CategorizationData
import com.monoexpenses.domain.model.CategorizedTransactions
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.CategoryFilter
import com.monoexpenses.domain.model.Transaction

class CategorizeTransactionsUseCase {

    fun execute(transactions: List<Transaction>, categories: List<Category>): CategorizationData {
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

        val categorizedTransactions = categoriesMap.map { (category, transactions) ->
            CategorizedTransactions(
                category,
                transactions,
                totalExpenses = calcTotalExpenses(transactions)
            )
        }

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
        val matchedCategories = mutableListOf<Category>()
        categories.forEach { category ->
            if (isTransactionMatching(transaction, category.categoryFilters)) {
                matchedCategories.add(category)
            }
        }

        if (matchedCategories.size > 1) {
            throw IllegalStateException("Transaction is matched for a few categories. $transaction $matchedCategories")
        }
        return matchedCategories.firstOrNull()
    }

    private fun isTransactionMatching(
        transaction: Transaction,
        filters: List<CategoryFilter>,
    ): Boolean {
        return filters.any { filter ->
            val mccMatching = filter.transactionMcc?.equals(transaction.mcc) ?: true
            val amountMatching = filter.transactionAmount?.equals(transaction.amount) ?: true
            val descriptionMatching =
                filter.transactionDescription?.let { transaction.description.contains(it) } ?: true
            mccMatching && amountMatching && descriptionMatching
        }
    }
}