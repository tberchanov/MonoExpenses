package com.monoexpenses.data.accounts

import com.monoexpenses.data.database.DBProvider
import com.monoexpenses.domain.model.BankAccount

internal class BankAccountsLocalDataSourceImpl(
    private val database: DBProvider,
) : BankAccountsLocalDataSource {

    override suspend fun saveSelectedAccounts(userId: String, bankAccounts: List<BankAccount>) {
        bankAccounts.forEach {
            database.queries().insertAccount(
                accountId = it.id,
                userId = userId,
                name = it.name,
                currency = it.currency,
            )
        }
    }

    override suspend fun getSelectedAccounts(userId: String): List<BankAccount> {
        return database.queries()
            .getAccounts(userId)
            .executeAsList()
            .map {
                BankAccount(
                    id = it.accountId,
                    name = it.name,
                    currency = it.currency,
                )
            }
    }
}