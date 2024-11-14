package org.umcs.mobile

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}