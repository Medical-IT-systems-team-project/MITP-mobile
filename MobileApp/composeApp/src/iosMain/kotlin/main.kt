import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import org.umcs.mobile.navigation.NavigationHost
import org.umcs.mobile.network.GlobalKtorClient
import org.umcs.mobile.persistence.createDataStore
import org.umcs.mobile.theme.AppTheme
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    GlobalKtorClient.initClient()
    initKoin()
    return ComposeUIViewController {
        AppTheme {
            NavigationHost(
                loginDataStore = remember { createDataStore() },
                testDataStore = remember { createDataStore() }
            )
        }
    }
}

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}