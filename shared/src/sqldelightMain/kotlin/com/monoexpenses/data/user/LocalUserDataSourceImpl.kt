package com.monoexpenses.data.user

import com.monoexpenses.data.database.DBProvider
import com.monoexpenses.data.dto.LocalUserData

internal class LocalUserDataSourceImpl(
    private val database: DBProvider,
) : LocalUserDataSource {

    override suspend fun write(userId: String, userName: String) {
        database.queries().insertUserData(userId, userName)
    }

    override suspend fun getNewUserId(): String {
        val userDataQuantity = database.queries()
            .countUserData()
            .executeAsOne()
        return (userDataQuantity + 1).toString()
    }

    override suspend fun delete(userId: String) {
        database.queries().deleteUserData(userId)
    }

    override suspend fun getAllUsers(): List<LocalUserData> {
        return database.queries()
            .getAllUserData()
            .executeAsList()
            .map {
                LocalUserData(it.userId, it.userName ?: "")
            }
    }
}
