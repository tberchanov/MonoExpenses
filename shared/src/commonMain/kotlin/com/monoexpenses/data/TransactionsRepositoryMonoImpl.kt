package com.monoexpenses.data

import co.touchlab.kermit.Logger
import com.monoexpenses.data.dto.TransactionDto
import com.monoexpenses.data.network.httpClient
import com.monoexpenses.domain.model.Transaction
import com.monoexpenses.domain.repository.TransactionsRepository
import com.monoexpenses.exception.UnexpectedResponseException
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

private const val BASE_URL = "https://api.monobank.ua"

private const val TRANSACTIONS_LIMIT_PER_REQUEST = 500

internal class TransactionsRepositoryMonoImpl : TransactionsRepository {

    override suspend fun getStatement(
        token: String,
        accountId: String,
        fromMillis: Long,
        toMillis: Long,
    ): List<Transaction> {
        Logger.d("TransactionsRepository") {
            "getStatement $token $accountId $fromMillis $toMillis"
        }

        val response =
            httpClient.get("$BASE_URL/personal/statement/$accountId/$fromMillis/$toMillis") {
                headers {
                    append("X-Token", token)
                }
            }

        if (response.status.isSuccess()) {
            val transactionsDto: List<TransactionDto> = response.body()

            if (transactionsDto.size == TRANSACTIONS_LIMIT_PER_REQUEST) {
                throw IllegalStateException("Transactions limit")
            }

            return transactionsDto.map {
                Transaction(
                    id = it.id,
                    mcc = it.mcc,
                    amount = it.amount,
                    description = it.description,
                    time = it.time,
                    receiptId = it.receiptId,
                )
            }
        } else {
            throw UnexpectedResponseException(
                statusCode = response.status.value,
                body = response.bodyAsText(),
            )
        }
    }
}