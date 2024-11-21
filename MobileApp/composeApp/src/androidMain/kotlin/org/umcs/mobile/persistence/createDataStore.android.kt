package org.umcs.mobile.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createDataStore(context : Context) : DataStore<Preferences> {
    return createDataStore {
        context.filesDir.resolve("prefs.preferences_pb").absolutePath
    }
}