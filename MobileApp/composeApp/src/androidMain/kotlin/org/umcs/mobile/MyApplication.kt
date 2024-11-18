package org.umcs.mobile

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.umcs.mobile.persistence.createDataStore

class MyApplication : Application() {
    lateinit var loginDataStore: DataStore<Preferences>
    lateinit var testDataStore: DataStore<Preferences>

    override fun onCreate() {
        super.onCreate()
        loginDataStore = createDataStore(applicationContext)
        testDataStore = createDataStore(applicationContext)
        initKoin()
    }
}

