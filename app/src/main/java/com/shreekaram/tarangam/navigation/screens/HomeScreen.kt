package com.shreekaram.tarangam.navigation.screens

import android.annotation.SuppressLint
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shreekaram.tarangam.composables.BottomNavigationBar
import com.shreekaram.tarangam.navigation.Routes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController) {
    val homeNavController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = homeNavController) }
    ) { _ ->
        NavHost(
            navController = homeNavController,
            startDestination = Routes.MusicList.id,
            enterTransition = { EnterTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable(Routes.MusicList.id) {
                Scaffold(
                ) { _ ->
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Music List",
                            textAlign = TextAlign.Center,
                        )

                    }
                }
            }

            composable(Routes.Search.id) {
                Text(
                    text = "Search",
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}