package com.monoexpenses.data.accounts

import com.monoexpenses.data.dto.BankAccountsResponseDto
import com.monoexpenses.data.network.httpClient
import com.monoexpenses.domain.model.BankAccount
import com.monoexpenses.exception.UnexpectedResponseException
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

internal class BankAccountsNetworkDataSource {

    suspend fun loadAllAccounts(token: String): List<BankAccount> {
        val response = httpClient.get("$BASE_URL/personal/client-info") {
            headers {
                append("X-Token", token)
            }
        }

        if (response.status.isSuccess()) {
            val accountsResponse: BankAccountsResponseDto = response.body()
            return accountsResponse.accounts.map { accountDto ->
                BankAccount(
                    id = accountDto.id,
                    name = accountDto.name,
                    currency = currencyCodeToNameMap[accountDto.currencyCode]
                        ?: accountDto.currencyCode.toString(),
                )
            }
        } else {
            throw UnexpectedResponseException(
                statusCode = response.status.value,
                body = response.bodyAsText(),
            )
        }
    }

    companion object {
        private const val BASE_URL = "https://api.monobank.ua"
        private val currencyCodeToNameMap = mapOf(
            980 to "UAH",
            840 to "USD",
            978 to "EUR",
        )
    }
}
