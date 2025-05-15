package com.monoexpenses.domain.usecase

import com.monoexpenses.domain.model.BankAccount
import com.monoexpenses.domain.repository.BankAccountsRepository

class GetAllAccountsUseCase(
    private val bankAccountsRepository: BankAccountsRepository,
) {

    suspend fun execute(token: String): List<BankAccount> {
        return bankAccountsRepository.loadAllAccounts(token)
            .sortedWith(compareBy<BankAccount> { it.name }.thenBy { it.currency })
    }
}