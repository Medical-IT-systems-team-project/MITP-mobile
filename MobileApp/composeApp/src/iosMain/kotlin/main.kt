import androidx.compose.ui.window.ComposeUIViewController
import network.chaintech.cmpimagepickncrop.platform
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