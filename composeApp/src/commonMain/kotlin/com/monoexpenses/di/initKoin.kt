package com.monoexpenses.di

import com.monoexpenses.presentation.add.accounts.AddAccountsViewModel
import com.monoexpenses.presentation.home.HomeViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            sharedModule,
        )
    }
}

val appModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::AddAccountsViewModel)
}
