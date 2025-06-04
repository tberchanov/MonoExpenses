package com.monoexpenses.data.accounts

import com.monoexpenses.data.browser.storage.localStorage
import com.monoexpenses.domain.model.BankAccount
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class BankAccountsLocalDataSourceImpl : BankAccountsLocalDataSource {
    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    override suspend fun saveSelectedAccounts(userId: String, bankAccounts: List<BankAccount>) {
        val key = "selected_accounts_$userId"
        val dtos = bankAccounts.map { it.toDto() }
        val jsonString = json.encodeToString(dtos)
        localStorage.setItem(key, jsonString)
    }

    override suspend fun getSelectedAccounts(userId: String): List<BankAccount> {
        val key = "selected_accounts_$userId"
        val jsonString = localStorage.getItem(key) ?: return emptyList()
        return try {
            val dtos = json.decodeFromString<List<BankAccountDto>>(jsonString)
            dtos.map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

@Serializable
private data class BankAccountDto(
    val id: String,
    val name: String,
    val currency: String,
)

private fun BankAccount.toDto() = BankAccountDto(
    id = id,
    name = name,
    currency = currency,
)

private fun BankAccountDto.toDomain() = BankAccount(
    id = id,
    name = name,
    currency = currency,
)
