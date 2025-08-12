package com.monoexpenses.domain.usecase

import com.monoexpenses.domain.model.BankAccount
import com.monoexpenses.domain.model.UserBankAccounts
import com.monoexpenses.domain.repository.BankAccountsRepository

class GetAllAccountsUseCase(
    private val bankAccountsRepository: BankAccountsRepository,
) {

    suspend fun execute(token: String): UserBankAccounts {
        return bankAccountsRepository.loadAllAccounts(token).run {
            copy(
                bankAccounts = bankAccounts.sortedWith(compareBy<BankAccount> { it.name }.thenBy { it.currency }),
            )
        }
    }
}