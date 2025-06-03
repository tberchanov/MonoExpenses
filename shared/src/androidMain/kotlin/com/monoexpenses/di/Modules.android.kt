package com.monoexpenses.di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.monoexpenses.Database
import com.monoexpenses.data.database.DBProvider
import com.monoexpenses.data.di.sqldelightDataModule
import com.monoexpenses.data.secure.SecureUserDataSourceImpl
import com.monoexpenses.data.user.SecureUserDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

private const val DB_NAME = "database.db"

internal actual fun getPlatformModule() = module {
    single {
        DBProvider {
            val driver = AndroidSqliteDriver(
                Database.Schema.synchronous(),
                androidContext(),
                DB_NAME,
            )
            Database.Schema.create(driver).await()
            Database(driver)
        }
    }
    includes(sqldelightDataModule)
    singleOf(::SecureUserDataSourceImpl).bind<SecureUserDataSource>()
}
