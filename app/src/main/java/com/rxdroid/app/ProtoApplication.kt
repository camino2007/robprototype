package com.rxdroid.app

import android.app.Application
import com.rxdroid.app.di.appModuleList
import org.koin.android.ext.android.startKoin

class ProtoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModuleList)
    }
}


