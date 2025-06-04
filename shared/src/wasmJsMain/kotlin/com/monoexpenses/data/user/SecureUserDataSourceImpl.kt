package com.monoexpenses.data.user

import co.touchlab.kermit.Logger
import com.monoexpenses.data.browser.storage.BrowserStorage
import com.monoexpenses.data.browser.storage.localStorage

private const val TAG = "SecureUserDataSource"
private const val KEY_PREFIX = "sec_"

class SecureUserDataSourceImpl : SecureUserDataSource {

    /*
    * Would be more secure to use `sessionStorage`.
    * However with `sessionStorage` data will be lost for each new tab.
    * Because of that `localStorage` is used currently as a temporal solution.
    * */
    private val secureStorage: BrowserStorage = localStorage

    override suspend fun readToken(userId: String): String? {
        return secureStorage.getItem(KEY_PREFIX + userId)
    }

    override suspend fun writeToken(userId: String, token: String) {
        Logger.d(TAG) { "writeToken: $userId" }
        secureStorage.setItem(KEY_PREFIX + userId, token)
    }

    override suspend fun deleteToken(userId: String) {
        secureStorage.removeItem(KEY_PREFIX + userId)
    }
}