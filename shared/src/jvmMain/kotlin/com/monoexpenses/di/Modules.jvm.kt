package com.monoexpenses.di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.monoexpenses.Database
import com.monoexpenses.data.database.DBProvider
import com.monoexpenses.data.di.sqldelightDataModule
import com.monoexpenses.data.user.SecureUserDataSource
import com.monoexpenses.data.user.SecureUserDataSourceImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import java.io.File
import java.util.Properties

internal actual fun getPlatformModule(): Module = module {
    single {
        DBProvider {
            val dbPath = File(System.getProperty("user.home"), ".monoexpenses/database.db").apply {
                parentFile?.mkdirs()
            }.absolutePath
            
            val driver: SqlDriver = JdbcSqliteDriver(
                "jdbc:sqlite:$dbPath",
                Properties(),
                Database.Schema.synchronous(),
            )
            Database.Schema.create(driver).await()
            Database(driver)
        }
    }
    includes(sqldelightDataModule)
    singleOf(::SecureUserDataSourceImpl).bind<SecureUserDataSource>()
}
