package com.monoexpenses.data.user

import com.monoexpenses.domain.model.UserData
import com.monoexpenses.domain.repository.UserDataRepository

internal class UserDataRepositoryImpl(
    private val localUserDataSource: LocalUserDataSource,
    private val secureUserDataSource: SecureUserDataSource,
) : UserDataRepository {

    override suspend fun saveUserData(userData: UserData) {
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
        return ids.map {
            val token = secureUserDataSource.readToken(it)
            checkNotNull(token)
            UserData(
                id = it,
                token = token,
            )
        }
    }
}
