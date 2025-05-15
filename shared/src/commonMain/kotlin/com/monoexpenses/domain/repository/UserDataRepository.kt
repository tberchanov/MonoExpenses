package com.monoexpenses.domain.repository

import com.monoexpenses.domain.model.UserData

interface UserDataRepository {

    suspend fun saveUserData(userData: UserData)

    suspend fun getNewUserDataId(): String

    suspend fun deleteUserData(userId: String)

    suspend fun getAllUserData(): List<UserData>
}