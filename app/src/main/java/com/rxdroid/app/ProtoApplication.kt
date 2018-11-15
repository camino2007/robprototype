package com.rxdroid.app

import android.app.Application
import com.rxdroid.app.di.appModuleList
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class ProtoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModuleList)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}


