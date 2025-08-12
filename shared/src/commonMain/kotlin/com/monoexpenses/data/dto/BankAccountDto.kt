package com.monoexpenses.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class BankAccountDto(
    @SerialName("id")
    val id: String,
    @SerialName("type")
    val name: String,
    @SerialName("currencyCode")
    val currencyCode: Int,
    @SerialName("maskedPan")
    val maskedPan: List<String>
)
