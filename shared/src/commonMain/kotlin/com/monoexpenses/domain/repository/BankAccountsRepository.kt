package com.monoexpenses.domain.repository

import com.monoexpenses.domain.model.BankAccount

interface BankAccountsRepository {
    suspend fun loadSelectedAccounts(userId: String): List<BankAccount>

    suspend fun saveSelectedAccounts(userId: String, bankAccounts: List<BankAccount>)

    suspend fun loadAllAccounts(token: String): List<BankAccount>
}