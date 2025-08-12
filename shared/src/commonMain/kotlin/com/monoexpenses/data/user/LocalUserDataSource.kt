package com.monoexpenses.data.user

import com.monoexpenses.data.dto.LocalUserData

internal interface LocalUserDataSource {
    suspend fun write(userId: String, userName: String)
    suspend fun getNewUserId(): String
    suspend fun delete(userId: String)
    suspend fun getAllUsers(): List<LocalUserData>
}
