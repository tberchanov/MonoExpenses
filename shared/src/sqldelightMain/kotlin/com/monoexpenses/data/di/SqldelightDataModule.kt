package com.monoexpenses.data.di

import com.monoexpenses.data.accounts.BankAccountsLocalDataSource
import com.monoexpenses.data.accounts.BankAccountsLocalDataSourceImpl
import com.monoexpenses.data.category.CategoriesLocalDataSource
import com.monoexpenses.data.category.CategoriesLocalDataSourceImpl
import com.monoexpenses.data.user.LocalUserDataSource
import com.monoexpenses.data.user.LocalUserDataSourceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sqldelightDataModule = module {
    singleOf(::LocalUserDataSourceImpl).bind<LocalUserDataSource>()
    singleOf(::BankAccountsLocalDataSourceImpl).bind<BankAccountsLocalDataSource>()
    singleOf(::CategoriesLocalDataSourceImpl).bind<CategoriesLocalDataSource>()
}
