package org.umcs.mobile.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.umcs.mobile.App

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

@Composable
fun SecondScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.DarkGray).clickable(onClick = {
            navController.navigateUp()
        }),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "SECOND SCREEN", color = Color.LightGray, fontSize = 30.sp)
    }
}
