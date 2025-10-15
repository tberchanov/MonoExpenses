package com.monoexpenses.domain.usecase

import com.monoexpenses.domain.model.CategorizationData
import com.monoexpenses.domain.model.CategorizedTransactions
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.TransactionFullData

class MoveTransactionToCategoryUseCase {

    fun execute(
        categorizationData: CategorizationData,
        transactionToMove: TransactionFullData,
        category: Category,
    ): CategorizationData {
        val uncategorizedTransactions = categorizationData.uncategorizedTransactions
            .toMutableList()
            .apply { remove(transactionToMove) }

        val categorizedTransactions = categorizationData.categorizedTransactions.firstOrNull() {
            it.category.id == category.id
        }

        val categorizedTransactionsList = categorizationData.categorizedTransactions.toMutableList()
        if (categorizedTransactions == null) {
            categorizedTransactionsList.add(
                CategorizedTransactions(
                    category,
                    listOf(transactionToMove),
                    transactionToMove.transaction.amount,
                )
            )
        } else {
            categorizedTransactionsList.remove(categorizedTransactions)
            categorizedTransactionsList.add(categorizedTransactions.addTransaction(transactionToMove))
        }

        return categorizationData.copy(
            categorizedTransactions = categorizedTransactionsList,
            uncategorizedTransactions = uncategorizedTransactions,
            uncategorizedTotalExpenses = categorizationData.uncategorizedTotalExpenses - transactionToMove.transaction.amount,
        )
    }

    private fun CategorizedTransactions.addTransaction(transactionToAdd: TransactionFullData): CategorizedTransactions {
        val totalExpenses = totalExpenses + transactionToAdd.transaction.amount
        val transactions = transactions.toMutableList().apply { add(transactionToAdd) }
        return copy(
            transactions = transactions,
            totalExpenses = totalExpenses
        )
    }
}