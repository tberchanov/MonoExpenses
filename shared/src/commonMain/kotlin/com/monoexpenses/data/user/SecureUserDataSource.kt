package com.monoexpenses.data.user

internal interface SecureUserDataSource {
    suspend fun readToken(userId: String): String?
    suspend fun writeToken(userId: String, token: String)
    suspend fun deleteToken(userId: String)
}
