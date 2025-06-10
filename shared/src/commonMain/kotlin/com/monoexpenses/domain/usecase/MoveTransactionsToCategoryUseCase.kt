package com.monoexpenses.domain.usecase

import com.monoexpenses.domain.model.CategorizationData
import com.monoexpenses.domain.model.Category
import com.monoexpenses.domain.model.Transaction

class MoveTransactionsToCategoryUseCase(
    private val moveTransactionToCategoryUseCase: MoveTransactionToCategoryUseCase,
) {

    fun execute(
        categorizationData: CategorizationData,
        transactionsToMove: List<Transaction>,
        category: Category,
    ): CategorizationData {
        var modifiedCategorizationData = categorizationData
        transactionsToMove.forEach { transactionToMove ->
            modifiedCategorizationData = moveTransactionToCategoryUseCase.execute(
                modifiedCategorizationData,
                transactionToMove,
                category,
            )
        }
        return modifiedCategorizationData
    }
}