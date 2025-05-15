package com.monoexpenses.domain.repository

import com.monoexpenses.domain.model.Transaction

interface TransactionsRepository {

    suspend fun getStatement(
        token: String,
        accountId: String,
        fromMillis: Long,
        toMillis: Long,
    ): List<Transaction>
}