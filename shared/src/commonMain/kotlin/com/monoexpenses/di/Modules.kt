package com.monoexpenses.di

import com.monoexpenses.data.CategoryRepositoryImpl
import com.monoexpenses.data.TransactionsRepositoryMonoImpl
import com.monoexpenses.data.accounts.BankAccountsNetworkDataSource
import com.monoexpenses.data.accounts.BankAccountsRepositoryImpl
import com.monoexpenses.data.user.UserDataRepositoryImpl
import com.monoexpenses.domain.repository.BankAccountsRepository
import com.monoexpenses.domain.repository.CategoryRepository
import com.monoexpenses.domain.repository.TransactionsRepository
import com.monoexpenses.domain.repository.UserDataRepository
import com.monoexpenses.domain.usecase.CategorizeTransactionsUseCase
import com.monoexpenses.domain.usecase.GetAllAccountsUseCase
import com.monoexpenses.domain.usecase.GetTransactionsUseCase
import com.monoexpenses.domain.usecase.MoveTransactionToCategoryUseCase
import com.monoexpenses.domain.usecase.SaveSelectedAccountsUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val useCaseModule = module {
    factoryOf(::GetTransactionsUseCase)
    factoryOf(::CategorizeTransactionsUseCase)
    factoryOf(::GetAllAccountsUseCase)
    factoryOf(::SaveSelectedAccountsUseCase)
    factoryOf(::MoveTransactionToCategoryUseCase)
}

internal val repositoryModule = module {
    singleOf(::TransactionsRepositoryMonoImpl).bind<TransactionsRepository>()
    singleOf(::BankAccountsRepositoryImpl).bind<BankAccountsRepository>()
    singleOf(::CategoryRepositoryImpl).bind<CategoryRepository>()
    singleOf(::UserDataRepositoryImpl).bind<UserDataRepository>()
    singleOf(::BankAccountsNetworkDataSource)
}

internal expect fun getPlatformModule(): Module

val sharedModule = module {
    includes(
        getPlatformModule(),
        useCaseModule,
        repositoryModule,
    )
}
