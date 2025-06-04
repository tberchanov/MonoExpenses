package com.monoexpenses.di

import com.monoexpenses.data.accounts.BankAccountsLocalDataSource
import com.monoexpenses.data.accounts.BankAccountsLocalDataSourceImpl
import com.monoexpenses.data.user.LocalUserDataSource
import com.monoexpenses.data.user.LocalUserDataSourceImpl
import com.monoexpenses.data.user.SecureUserDataSource
import com.monoexpenses.data.user.SecureUserDataSourceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual fun getPlatformModule() = module {
    singleOf(::LocalUserDataSourceImpl).bind<LocalUserDataSource>()
    singleOf(::BankAccountsLocalDataSourceImpl).bind<BankAccountsLocalDataSource>()
    singleOf(::SecureUserDataSourceImpl).bind<SecureUserDataSource>()
}
