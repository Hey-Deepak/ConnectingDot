package com.ts.connectingdot

import android.app.Application
import com.ts.connectingdot.helper.di.appModule
import com.ts.connectingdot.helper.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ConnectingDotApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ConnectingDotApp)
            modules(appModule, viewModelModule)
        }

    }
}