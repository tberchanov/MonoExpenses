package com.monoexpenses.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class BankAccountsResponseDto(
    @SerialName("name")
    val name: String,
    @SerialName("accounts")
    val accounts: List<BankAccountDto>,
)
