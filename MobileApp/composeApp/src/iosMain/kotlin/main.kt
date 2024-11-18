import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import org.umcs.mobile.navigation.NavigationHost
import org.umcs.mobile.persistence.createDataStore
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController { NavigationHost(
        loginDataStore = remember { createDataStore() },
        testDataStore = remember { createDataStore() }
    ) }
}

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}