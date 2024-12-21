import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.compose.rememberNavController
import org.appModule
import org.koin.core.context.startKoin
import org.umcs.mobile.navigation.NavigationHost
import org.umcs.mobile.persistence.createDataStore
import org.umcs.mobile.theme.AppTheme
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin()
    return ComposeUIViewController {
        val s = rememberNavController()
        AppTheme {
            NavigationHost(
                loginDataStore = remember { createDataStore() },
                testDataStore = remember { createDataStore() }
            )
        }
    }
}

/*
* class GestureViewController(
    private val onBackPressed: () -> Unit
) : UIViewController() {
    override fun viewDidLoad() {
        super.viewDidLoad()

        val edgePanGesture = UIScreenEdgePanGestureRecognizer(target = this, action = NSSelectorFromString("handleEdgePan:"))
        edgePanGesture.edges = UIRectEdgeRight
        view.addGestureRecognizer(edgePanGesture)
    }

    @ObjCAction
    fun handleEdgePan(gesture: UIScreenEdgePanGestureRecognizer) {
        if (gesture.state == UIGestureRecognizerStateEnded) {
            onBackPressed()
        }
    }
}

fun MainViewController(): UIViewController {
    GlobalKtorClient.initClient()
    initKoin()
    return ComposeUIViewController {
        AppTheme {
            val navController = rememberNavController()
            GestureViewController(onBackPressed = navController::navigateUp).apply {
                setContent {
                    NavigationHost(
                        navController = navController,
                        loginDataStore = remember { createDataStore() },
                        testDataStore = remember { createDataStore() }
                    )
                }
            }
        }
    }
}

* */

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}