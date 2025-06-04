package com.monoexpenses.data.user

import com.monoexpenses.data.browser.storage.localStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalUserDataSourceImpl : LocalUserDataSource {
    companion object {
        private const val USER_IDS_KEY = "user_ids"
        private const val ID_LENGTH = 8
    }

    override suspend fun write(userId: String) = withContext(Dispatchers.Main) {
        val currentIds = getAllIds().toMutableList()
        if (!currentIds.contains(userId)) {
            currentIds.add(userId)
            localStorage.setItem(USER_IDS_KEY, currentIds.joinToString(","))
        }
    }

    override suspend fun getNewUserId(): String = withContext(Dispatchers.Main) {
        val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val newId = (1..ID_LENGTH)
            .map { chars.random() }
            .joinToString("")
        
        write(newId)
        newId
    }

    override suspend fun delete(userId: String) = withContext(Dispatchers.Main) {
        val currentIds = getAllIds().toMutableList()
        if (currentIds.remove(userId)) {
            localStorage.setItem(USER_IDS_KEY, currentIds.joinToString(","))
        }
    }

    override suspend fun getAllIds(): List<String> = withContext(Dispatchers.Main) {
        localStorage.getItem(USER_IDS_KEY)?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
    }
}