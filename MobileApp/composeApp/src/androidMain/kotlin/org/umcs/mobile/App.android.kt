package org.umcs.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import appModule
import org.koin.core.context.startKoin
import org.umcs.mobile.navigation.NavigationHost

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initKoin()
        enableEdgeToEdge()
        setContent { NavigationHost() }
    }
}

internal fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
