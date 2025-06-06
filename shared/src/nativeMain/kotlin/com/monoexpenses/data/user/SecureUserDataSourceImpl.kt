package com.monoexpenses.data.user

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

private const val KEY_PREFIX = "sec_"

@OptIn(ExperimentalSettingsImplementation::class)
internal class SecureUserDataSourceImpl : SecureUserDataSource {

    private val settings by lazy {
        KeychainSettings(
            service = "EncryptedSettings"
        )
    }

    override suspend fun readToken(userId: String): String? {
        return settings[KEY_PREFIX + userId]
    }

    override suspend fun writeToken(userId: String, token: String) {
        settings[KEY_PREFIX + userId] = token
    }

    override suspend fun deleteToken(userId: String) {
        settings.remove(KEY_PREFIX + userId)
    }
}