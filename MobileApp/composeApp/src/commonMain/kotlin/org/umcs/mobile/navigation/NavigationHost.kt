package org.umcs.mobile.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import org.umcs.mobile.App
import org.umcs.mobile.composables.LoginScreen
import org.umcs.mobile.theme.AppTheme
import qrscanner.CameraLens
import qrscanner.OverlayShape
import qrscanner.QrScanner
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@Composable
fun NavigationHost(navController: NavHostController = rememberNavController()) {

    AppTheme{
        NavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            startDestination = "home"
        ) {
            composable(Routes.HOME) {
                App(navController)
            }
            composable(Routes.SECOND) {
                SecondScreen(navController)
            }
            composable(Routes.THIRD) {
                ThirdScreen(navController)
            }
            composable(Routes.LOGIN){
                LoginScreen(goToHomeScreen = { navController.navigate(Routes.HOME) })
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun SecondScreen(navController: NavHostController) {
    val uuid = Uuid.random().toString()
    Logger.i("UUID: $uuid", tag = "UUID")
    Box(
        modifier = Modifier.fillMaxSize().background(Color.DarkGray).clickable(onClick = {
            navController.navigateUp()
        }),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(0.8f).aspectRatio(1f).background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(0.7f).aspectRatio(1f).zIndex(2f),
                painter = rememberQrCodePainter(data = uuid),
                contentDescription = "QR code referring to the example.com website"
            )
        }
    }
}

@Composable
fun ThirdScreen(navController: NavHostController) {
    var shownText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.DarkGray).clickable(onClick = {
        }),
    ) {
        QrScanner(
            modifier = Modifier.fillMaxWidth(0.8f).aspectRatio(1f).align(Alignment.TopCenter)
                .padding(top = 20.dp),
            flashlightOn = false,
            cameraLens = CameraLens.Back,
            openImagePicker = false,
            onCompletion = { UUID ->
                shownText = UUID
                Logger.i(UUID, tag = "UUID")
            },
            imagePickerHandler = { bool ->
                Logger.i("Image picker handler", tag = "Image picker handler")
            },
            onFailure = { failure ->
                shownText = failure
                Logger.i(failure, tag = "FAILURE")
            },
            overlayShape = OverlayShape.Square,
            overlayBorderColor = Color.Green,
        )

        val isVisible by derivedStateOf {
            shownText.isNotEmpty()
        }

        AnimatedVisibility(
            visible = isVisible,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp)
        ){
            Text(text = shownText, color = Color.White, fontSize = 22.sp, modifier = Modifier.clickable { navController.navigateUp() })
        }
    }
}
