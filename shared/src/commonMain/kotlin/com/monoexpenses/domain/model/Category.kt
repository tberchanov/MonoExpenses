package com.monoexpenses.domain.model

data class Category(
    val id: String,
    val name: String,
    val categoryFilters: List<CategoryFilter>,
)


data class CategoryFilter(
    val id: String,
    val transactionMcc: Int? = null,
    val transactionDescription: String? = null,
    val transactionAmount: Long? = null,
)
