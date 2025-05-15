package com.monoexpenses.domain.usecase

import com.monoexpenses.domain.model.Transaction
import com.monoexpenses.domain.model.UserData
import com.monoexpenses.domain.repository.BankAccountsRepository
import com.monoexpenses.domain.repository.TransactionsRepository
import com.monoexpenses.domain.repository.UserDataRepository

class GetTransactionsUseCase(
    private val bankAccountsRepository: BankAccountsRepository,
    private val transactionsRepository: TransactionsRepository,
    private val userDataRepository: UserDataRepository,
) {

    suspend fun execute(fromMillis: Long, toMillis: Long): List<Transaction> {
        return userDataRepository.getAllUserData()
            .flatMap { userData ->
                // FIXME load transactions for each user in parallel
                getAllTransactionsForUser(userData, fromMillis, toMillis)
            }
    }

    private suspend fun getAllTransactionsForUser(
        userData: UserData,
        fromMillis: Long,
        toMillis: Long,
    ): List<Transaction> {
        val bankAccounts = bankAccountsRepository.loadSelectedAccounts(userData.id)
        return bankAccounts.flatMap { bankAccount ->
            // FIXME load transactions for each bank account in parallel
            transactionsRepository.getStatement(
                userData.token,
                bankAccount.id,
                fromMillis,
                toMillis,
            )
        }
    }
}