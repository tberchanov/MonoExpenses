package com.monoexpenses.domain.model

data class TransactionFullData(
    val transaction: Transaction,
    val account: BankAccount,
    val userData: UserData,
)
