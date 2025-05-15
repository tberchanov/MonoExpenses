package com.monoexpenses.domain.usecase

import com.monoexpenses.domain.model.BankAccount
import com.monoexpenses.domain.model.UserData
import com.monoexpenses.domain.repository.BankAccountsRepository
import com.monoexpenses.domain.repository.UserDataRepository

class SaveSelectedAccountsUseCase(
    private val userDataRepository: UserDataRepository,
    private val accountsRepository: BankAccountsRepository,
) {

    suspend fun execute(token: String, accounts: List<BankAccount>) {
        val userId = userDataRepository.getNewUserDataId()
        userDataRepository.saveUserData(UserData(userId, token))
        accountsRepository.saveSelectedAccounts(userId, accounts)
    }
}