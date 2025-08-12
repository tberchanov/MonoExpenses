package com.monoexpenses.domain.model

data class CategorizationData(
    val categorizedTransactions: List<CategorizedTransactions>,
    val uncategorizedTransactions: List<TransactionFullData>,
    val uncategorizedTotalExpenses: Long,
)
