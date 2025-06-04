package com.monoexpenses.data.user

import co.touchlab.kermit.Logger
import com.monoexpenses.domain.model.UserData
import com.monoexpenses.domain.repository.UserDataRepository

private const val TAG = "UserDataRepository"

internal class UserDataRepositoryImpl(
    private val localUserDataSource: LocalUserDataSource,
    private val secureUserDataSource: SecureUserDataSource,
) : UserDataRepository {

    override suspend fun saveUserData(userData: UserData) {
        Logger.d(TAG) { "saveUserData: ${userData.id}" }
        localUserDataSource.write(userData.id)
        secureUserDataSource.writeToken(userData.id, userData.token)
    }

    override suspend fun getNewUserDataId(): String {
        return localUserDataSource.getNewUserId()
    }

    override suspend fun deleteUserData(userId: String) {
        localUserDataSource.delete(userId)
        secureUserDataSource.deleteToken(userId)
    }

    override suspend fun getAllUserData(): List<UserData> {
        val ids = localUserDataSource.getAllIds()
        val data = ids.map {
            val token = secureUserDataSource.readToken(it)
            checkNotNull(token)
            UserData(
                id = it,
                token = token,
            )
        }
        Logger.d(TAG) { "getAllUserData: ${data.size}" }
        return data
    }
}
