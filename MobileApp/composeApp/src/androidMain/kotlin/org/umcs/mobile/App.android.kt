package org.umcs.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import appModule
import org.koin.core.context.startKoin
import org.umcs.mobile.composables.LoginScreen
import org.umcs.mobile.navigation.NavigationHost
import org.umcs.mobile.theme.AppTheme

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { NavigationHost() }
    }
}

internal fun initKoin() {
    startKoin {
        modules(appModule)

    }
}

@Preview(showSystemUi = true)
@Composable
private fun NavigationHostPreviewDark() {
    AppTheme(systemIsDark = true){
        LoginScreen(
            goToHomeScreen = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun NavigationHostPreviewLight() {
    AppTheme(systemIsDark = false){
        LoginScreen(
            goToHomeScreen = {}
        )
    }
}