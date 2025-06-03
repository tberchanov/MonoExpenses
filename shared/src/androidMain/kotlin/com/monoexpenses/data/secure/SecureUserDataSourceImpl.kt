package com.monoexpenses.data.secure

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import com.monoexpenses.data.user.SecureUserDataSource
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

class SecureUserDataSourceImpl(context: Context) : SecureUserDataSource {

    private val settings by lazy { getSettings(context) }

    override suspend fun readToken(userId: String): String? {
        return settings.getString(userId, "").ifEmpty { null }
    }

    override suspend fun writeToken(userId: String, token: String) {
        settings.putString(userId, token)
    }

    override suspend fun deleteToken(userId: String) {
        settings.remove(userId)
    }
}

private fun getSettings(context: Context): Settings {
    val encryptedPreferences = EncryptedSharedPreferences.create(
        context.packageName + "_encrypted_preferences",
        "master",
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    return SharedPreferencesSettings(encryptedPreferences)
}
