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
import androidx.navigation.toRoute
import co.touchlab.kermit.Logger
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import org.umcs.mobile.App
import org.umcs.mobile.composables.LoginScreen
import org.umcs.mobile.composables.QrCodeScanner
import org.umcs.mobile.composables.case_list_view.CaseListLayout
import org.umcs.mobile.composables.case_view.CaseLayout
import org.umcs.mobile.composables.new_case_view.NewCaseLayout
import org.umcs.mobile.theme.AppTheme
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@Composable
fun NavigationHost(navController: NavHostController = rememberNavController()) {

    AppTheme{
        NavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            startDestination = Routes.HOME
        ) {
            composable(Routes.HOME) {
                App(navController)
            }
            composable(Routes.CASE_LIST) {
                CaseListLayout(
                    navigateToCase = navController::navigate,
                    navigateBack = navController::navigateUp
                )
            }
            composable(Routes.NEW_CASE) {
                NewCaseLayout()
            }
            composable(Routes.THIRD) {
                QrCodeScanner(navController)
            }
            composable(Routes.LOGIN){
                LoginScreen(goToHomeScreen = { navController.navigate(Routes.HOME) })
            }
            composable<Case>{ backStackEntry ->
                val case : Case = backStackEntry.toRoute()
                CaseLayout(case)
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

