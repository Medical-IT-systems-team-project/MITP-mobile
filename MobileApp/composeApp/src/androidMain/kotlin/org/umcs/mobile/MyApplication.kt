package org.umcs.mobile

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import co.touchlab.kermit.Logger
import com.theapache64.rebugger.RebuggerConfig
import org.umcs.mobile.persistence.createDataStore

class MyApplication : Application() {
    lateinit var loginDataStore: DataStore<Preferences>
    lateinit var testDataStore: DataStore<Preferences>

    override fun onCreate() {
        super.onCreate()
        loginDataStore = createDataStore(applicationContext)
        testDataStore = createDataStore(applicationContext)
        initKoin()
        RebuggerConfig.init(
            tag = "Rebugger",
            logger = { tag, message -> Logger.i(message,tag = tag) }
        )
    }
}

