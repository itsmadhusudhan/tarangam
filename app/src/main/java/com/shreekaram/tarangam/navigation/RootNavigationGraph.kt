package com.shreekaram.tarangam.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun HomeScreen() {
    Text(text = "Tarangam!", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
}

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Home.id) {
        composable(Routes.Home.id) {
            HomeScreen()
        }
    }
}