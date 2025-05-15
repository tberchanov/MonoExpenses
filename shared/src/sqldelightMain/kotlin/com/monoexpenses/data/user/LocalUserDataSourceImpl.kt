package com.monoexpenses.data.user

import com.monoexpenses.data.database.DBProvider

internal class LocalUserDataSourceImpl(
    private val database: DBProvider,
) : LocalUserDataSource {

    override suspend fun write(userId: String) {
        database.queries().insertUserData(userId)
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

    override suspend fun getAllIds(): List<String> {
        return database.queries().getAllUserData().executeAsList()
    }
}
