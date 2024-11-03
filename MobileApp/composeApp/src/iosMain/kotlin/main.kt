import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.startKoin
import org.umcs.mobile.navigation.NavigationHost
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController { NavigationHost() }
}

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}