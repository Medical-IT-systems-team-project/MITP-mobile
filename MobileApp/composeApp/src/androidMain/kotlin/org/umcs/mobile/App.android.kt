package org.umcs.mobile

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import org.appModule
import org.koin.core.context.startKoin
import org.umcs.mobile.navigation.NavigationHost
import org.umcs.mobile.theme.AppTheme

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myApplication = application as MyApplication

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT
            )
        )
        setContent {
            AppTheme {
                NavigationHost(
                    doctorLoginDataStore = remember { myApplication.loginDataStore },
                    patientLoginDataStore = remember { myApplication.testDataStore }
                )
            }
        }
    }
}

internal fun initKoin() {
    startKoin {
        modules(appModule)
    }
}

/*@Preview(showSystemUi = true)
@Composable
private fun LoginScreenPreviewDark() {
    AppTheme(systemIsDark = true) {
        DoctorLoginLayout(
            navigateToCaseList = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoginScreenPreviewLight() {
    AppTheme(systemIsDark = false) {
        DoctorLoginLayout(
            navigateToCaseList = {}
        )
    }
}*/

