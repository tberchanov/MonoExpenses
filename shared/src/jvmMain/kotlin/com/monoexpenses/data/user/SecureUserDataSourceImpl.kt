package com.monoexpenses.data.user

import com.microsoft.credentialstorage.StorageProvider
import com.microsoft.credentialstorage.model.StoredToken
import com.microsoft.credentialstorage.model.StoredTokenType

private const val TOKEN_KEY_PREFIX = "token_user_"

internal class SecureUserDataSourceImpl : SecureUserDataSource {

    private val tokenStorage by lazy {
        StorageProvider.getTokenStorage(
            true,
            StorageProvider.SecureOption.REQUIRED,
        )
    }

    override suspend fun readToken(userId: String): String? {
        return tokenStorage.get(TOKEN_KEY_PREFIX + userId)
            ?.value
            ?.let { String(it) }
    }

    override suspend fun writeToken(userId: String, token: String) {
        tokenStorage.add(
            TOKEN_KEY_PREFIX + userId,
            StoredToken(token.toCharArray(), StoredTokenType.PERSONAL),
        )
    }

    override suspend fun deleteToken(userId: String) {
        tokenStorage.delete(TOKEN_KEY_PREFIX + userId)
    }
}