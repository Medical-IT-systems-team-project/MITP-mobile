package org.umcs.mobile.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import org.umcs.mobile.App
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@Composable
fun NavigationHost(navController: NavHostController = rememberNavController()) {
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
