package com.monoexpenses.domain.usecase

import com.monoexpenses.domain.model.CategorizationData
import com.monoexpenses.domain.model.CategorizedTransactions
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.Transaction

class MoveTransactionToCategoryUseCase {

    fun execute(
        categorizationData: CategorizationData,
        transactionToMove: Transaction,
        category: Category,
    ): CategorizationData {
        val uncategorizedTransactions = categorizationData.uncategorizedTransactions
            .toMutableList()
            .apply { remove(transactionToMove) }

        val categorizedTransactions = categorizationData.categorizedTransactions.first {
            it.category == category
        }
        val categorizedTransactionsList = categorizationData.categorizedTransactions.toMutableList()
        categorizedTransactionsList.remove(categorizedTransactions)
        categorizedTransactionsList.add(categorizedTransactions.addTransaction(transactionToMove))

        return categorizationData.copy(
            categorizedTransactions = categorizedTransactionsList,
            uncategorizedTransactions = uncategorizedTransactions,
            uncategorizedTotalExpenses = categorizationData.uncategorizedTotalExpenses - transactionToMove.amount,
        )
    }

    private fun CategorizedTransactions.addTransaction(transactionToAdd: Transaction): CategorizedTransactions {
        val totalExpenses = totalExpenses + transactionToAdd.amount
        val transactions = transactions.toMutableList().apply { add(transactionToAdd) }
        return copy(
            transactions = transactions,
            totalExpenses = totalExpenses
        )
    }
}