package com.monoexpenses.data.accounts

import com.monoexpenses.domain.model.BankAccount

internal interface BankAccountsLocalDataSource {

    suspend fun saveSelectedAccounts(userId: String, bankAccounts: List<BankAccount>)

    suspend fun getSelectedAccounts(userId: String): List<BankAccount>
}