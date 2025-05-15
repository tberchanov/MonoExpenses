package com.monoexpenses.domain.model

data class CategorizedTransactions(
    val category: Category,
    val transactions: List<Transaction>,
    val totalExpenses: Long,
)
