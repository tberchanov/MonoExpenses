package com.monoexpenses.data.user

internal interface LocalUserDataSource {
    suspend fun write(userId: String)
    suspend fun getNewUserId(): String
    suspend fun delete(userId: String)
    suspend fun getAllIds(): List<String>
}
