import androidx.compose.ui.window.ComposeUIViewController
import org.umcs.mobile.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
