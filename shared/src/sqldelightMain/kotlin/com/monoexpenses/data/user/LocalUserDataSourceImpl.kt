package com.monoexpenses.data.user

import com.monoexpenses.data.database.DBProvider
import com.monoexpenses.data.dto.LocalUserData
import kotlin.random.Random

internal class LocalUserDataSourceImpl(
    private val database: DBProvider,
) : LocalUserDataSource {

    override suspend fun write(userId: String, userName: String) {
        database.queries().insertUserData(userId, userName)
    }

    override suspend fun getNewUserId(): String {
        return generateUuid()
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

private fun generateUuid(): String {
    fun randomHex(length: Int): String =
        (1..length).joinToString("") { Random.nextInt(0, 16).toString(16) }

    // UUID v4-style string (not cryptographically strong, but unique enough for IDs)
    return listOf(
        randomHex(8),
        randomHex(4),
        randomHex(4),
        randomHex(4),
        randomHex(12),
    ).joinToString("-")
}
