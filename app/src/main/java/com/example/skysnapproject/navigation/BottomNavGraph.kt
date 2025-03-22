package com.example.skysnapproject.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.skysnapproject.screens.AlarmScreen
import com.example.skysnapproject.screens.FavoriteScreen
import com.example.skysnapproject.screens.HomeScreen
import com.example.skysnapproject.screens.SettingsScreen
import com.example.skysnapproject.screens.SplashScreen

@Composable
fun BottomNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = "Splash"
    ) {
        composable("Splash") {
            SplashScreen(navController = navController)
        }
        composable(BottomBarRoutes.HomeRoute.title) {
            HomeScreen(paddingValues = paddingValues)
        }
        composable(BottomBarRoutes.FavRoute.title) {
            FavoriteScreen()
        }
        composable(BottomBarRoutes.AlarmRoute.title) {
            AlarmScreen()
        }
        composable(BottomBarRoutes.SettingsRoute.title) {
            SettingsScreen()
        }
    }
}