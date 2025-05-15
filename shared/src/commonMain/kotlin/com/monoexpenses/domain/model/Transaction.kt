package com.monoexpenses.domain.model

data class Transaction(
    val id: String,
    val mcc: Int,
    val amount: Long,
    val description: String,
    val time: Long,
    val receiptId: String?
)