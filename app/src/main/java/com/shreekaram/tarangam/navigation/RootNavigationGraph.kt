package com.shreekaram.tarangam.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shreekaram.tarangam.navigation.screens.HomeScreen

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Home.id) {
        composable(Routes.Home.id) {
            HomeScreen(navController = navController)
        }
    }
}