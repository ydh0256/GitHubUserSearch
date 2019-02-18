package com.android.duckkite.githubusersearch

import android.app.Application
import com.android.duckkite.githubusersearch.di.appModules
import org.koin.android.ext.android.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, appModules)
    }
}