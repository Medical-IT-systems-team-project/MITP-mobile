import androidx.compose.ui.window.ComposeUIViewController
import org.umcs.mobile.navigation.NavigationHost
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { NavigationHost() }
