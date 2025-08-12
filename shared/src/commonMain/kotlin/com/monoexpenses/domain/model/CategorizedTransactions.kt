package com.monoexpenses.domain.model

data class CategorizedTransactions(
    val category: Category,
    val transactions: List<TransactionFullData>,
    val totalExpenses: Long,
)
