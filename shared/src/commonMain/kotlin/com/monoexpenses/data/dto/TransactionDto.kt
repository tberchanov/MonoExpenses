package com.monoexpenses.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TransactionDto(
    @SerialName("id")
    val id: String,

    @SerialName("time")
    val time: Long,

    @SerialName("description")
    val description: String,

    @SerialName("mcc")
    val mcc: Int,

    @SerialName("originalMcc")
    val originalMcc: Int,

    @SerialName("amount")
    val amount: Long,

    @SerialName("operationAmount")
    val operationAmount: Long,

    @SerialName("currencyCode")
    val currencyCode: Int,

    @SerialName("commissionRate")
    val commissionRate: Long,

    @SerialName("cashbackAmount")
    val cashbackAmount: Long,

    @SerialName("balance")
    val balance: Long,

    @SerialName("hold")
    val hold: Boolean,

    @SerialName("receiptId")
    val receiptId: String? = null,

    @SerialName("comment")
    val comment: String? = null,

    @SerialName("invoiceId")
    val invoiceId: String? = null,
)