package com.monoexpenses

import android.app.Application
import com.monoexpenses.di.initKoin
import org.koin.android.ext.koin.androidContext

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@App)
        }
    }
}