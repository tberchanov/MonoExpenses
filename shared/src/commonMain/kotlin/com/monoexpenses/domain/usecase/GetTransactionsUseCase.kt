package com.monoexpenses.domain.usecase

import co.touchlab.kermit.Logger
import com.monoexpenses.domain.model.TransactionFullData
import com.monoexpenses.domain.model.UserData
import com.monoexpenses.domain.repository.BankAccountsRepository
import com.monoexpenses.domain.repository.TransactionsRepository
import com.monoexpenses.domain.repository.UserDataRepository

private const val TAG = "GetTransactionsUseCase"

class GetTransactionsUseCase(
    private val bankAccountsRepository: BankAccountsRepository,
    private val transactionsRepository: TransactionsRepository,
    private val userDataRepository: UserDataRepository,
) {

    suspend fun execute(fromMillis: Long, toMillis: Long): List<TransactionFullData> {
        Logger.d(TAG) { "execute $fromMillis $toMillis" }
        return filterEqualTokens(userDataRepository.getAllUserData())
            .flatMap { userData ->
                // FIXME load transactions for each user in parallel
                getAllTransactionsForUser(userData, fromMillis, toMillis)
            }
    }

    /*
    * Need to filter out equal tokens to not overload the API
    * */
    private fun filterEqualTokens(userDataList: List<UserData>): List<UserData> {
        return userDataList.groupBy { it.token }
            .map { (_, userDataList) -> userDataList.first() }
    }

    private suspend fun getAllTransactionsForUser(
        userData: UserData,
        fromMillis: Long,
        toMillis: Long,
    ): List<TransactionFullData> {
        Logger.d(TAG) { "getAllTransactionsForUser: ${userData.token} $fromMillis $toMillis" }
        val bankAccounts = bankAccountsRepository.loadSelectedAccounts(userData.id)
        return bankAccounts.flatMap { bankAccount ->
            // FIXME load transactions for each bank account in parallel
            transactionsRepository.getStatement(
                userData.token,
                bankAccount.id,
                fromMillis,
                toMillis,
            ).map { transaction ->
                TransactionFullData(
                    transaction, bankAccount, userData,
                )
            }
        }
    }
}
