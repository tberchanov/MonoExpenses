package com.monoexpenses.domain.usecase

import co.touchlab.kermit.Logger
import com.monoexpenses.domain.model.BankAccount
import com.monoexpenses.domain.model.UserData
import com.monoexpenses.domain.repository.BankAccountsRepository
import com.monoexpenses.domain.repository.UserDataRepository

private const val TAG = "SaveSelectedAccountsUseCase"

class SaveSelectedAccountsUseCase(
    private val userDataRepository: UserDataRepository,
    private val accountsRepository: BankAccountsRepository,
) {

    suspend fun execute(token: String, accounts: List<BankAccount>, userName: String) {
        Logger.d(TAG) { "execute ${accounts.size}" }
        val userId = userDataRepository.getNewUserDataId()
        userDataRepository.saveUserData(UserData(userId, token, userName))
        accountsRepository.saveSelectedAccounts(userId, accounts)
    }
}