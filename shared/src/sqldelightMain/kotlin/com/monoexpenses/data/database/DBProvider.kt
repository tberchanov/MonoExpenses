package com.monoexpenses.data.database

import com.monoexpenses.Database
import com.monoexpenses.DatabaseQueries

internal class DBProvider(
    private val creator: suspend () -> Database,
) {
    private var database: Database? = null

    suspend fun queries(): DatabaseQueries {
        // FIXME possible concurrency issue
        if (database == null) {
            database = creator()
        }
        return database!!.databaseQueries
    }
}