package com.monoexpenses.domain.model

data class BankAccount(
    val id: String,
    val name: String,
    val currency: String,
    val maskedPan: List<String>? = null,
)
