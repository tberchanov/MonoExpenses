package com.monoexpenses.domain.model

data class UserBankAccounts(
    val userData: UserData,
    val bankAccounts: List<BankAccount>,
)
