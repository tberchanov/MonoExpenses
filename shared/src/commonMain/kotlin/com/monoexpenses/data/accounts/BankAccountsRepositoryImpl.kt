package com.monoexpenses.data.accounts

import com.monoexpenses.domain.model.BankAccount
import com.monoexpenses.domain.repository.BankAccountsRepository

internal class BankAccountsRepositoryImpl(
    private val bankAccountsNetworkDataSource: BankAccountsNetworkDataSource,
    private val bankAccountsLocalDataSource: BankAccountsLocalDataSource,
) : BankAccountsRepository {

    override suspend fun loadSelectedAccounts(userId: String): List<BankAccount> {
        return bankAccountsLocalDataSource.getSelectedAccounts(userId)
    }

    override suspend fun saveSelectedAccounts(userId: String, bankAccounts: List<BankAccount>) {
        bankAccountsLocalDataSource.saveSelectedAccounts(userId, bankAccounts)
    }

    override suspend fun loadAllAccounts(token: String): List<BankAccount> {
        return bankAccountsNetworkDataSource.loadAllAccounts(token)
    }
}
