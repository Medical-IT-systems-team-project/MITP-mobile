package org.umcs.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import appModule
import org.koin.core.context.startKoin
import org.umcs.mobile.composables.login.DoctorLoginLayout
import org.umcs.mobile.navigation.NavigationHost
import org.umcs.mobile.theme.AppTheme

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            val myApplication = application as MyApplication
            NavigationHost(
                loginDataStore = remember {myApplication.loginDataStore},
                testDataStore = remember {myApplication.testDataStore}
            )
        }
    }
}

internal fun initKoin() {
    startKoin {
        modules(appModule)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoginScreenPreviewDark() {
    AppTheme(systemIsDark = true) {
        DoctorLoginLayout(
            goToHomeScreen = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoginScreenPreviewLight() {
    AppTheme(systemIsDark = false) {
        DoctorLoginLayout(
            goToHomeScreen = {},
        )
    }
}

