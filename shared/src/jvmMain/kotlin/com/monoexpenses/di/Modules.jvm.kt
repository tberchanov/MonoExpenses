package com.monoexpenses.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.monoexpenses.Database
import com.monoexpenses.data.accounts.BankAccountsLocalDataSource
import com.monoexpenses.data.accounts.BankAccountsLocalDataSourceImpl
import com.monoexpenses.data.database.DBProvider
import com.monoexpenses.data.user.LocalUserDataSource
import com.monoexpenses.data.user.LocalUserDataSourceImpl
import com.monoexpenses.data.user.SecureUserDataSource
import com.monoexpenses.data.user.SecureUserDataSourceImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual fun getPlatformModule(): Module = module {
    single {
        DBProvider {
            val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            Database.Schema.create(driver).await()
            Database(driver)
        }
    }
    singleOf(::LocalUserDataSourceImpl).bind<LocalUserDataSource>()
    singleOf(::SecureUserDataSourceImpl).bind<SecureUserDataSource>()
    singleOf(::BankAccountsLocalDataSourceImpl).bind<BankAccountsLocalDataSource>()
}
