package com.example.skysnapproject.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.skysnapproject.dataLayer.remote.RemoteDataSourceImpl
import com.example.skysnapproject.dataLayer.remote.RetrofitHelper
import com.example.skysnapproject.dataLayer.repo.Repository
import com.example.skysnapproject.screens.AlarmScreen
import com.example.skysnapproject.screens.FavoriteScreen
import com.example.skysnapproject.locationFeatch.HomeScreen
import com.example.skysnapproject.locationFeatch.WeatherViewModel
import com.example.skysnapproject.locationFeatch.WeatherViewModelFactory
import com.example.skysnapproject.screens.SettingsScreen
import com.example.skysnapproject.screens.SplashScreen
import kotlinx.coroutines.launch

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
            val viewModel: WeatherViewModel = viewModel(
                factory = WeatherViewModelFactory(
                    Repository.getInstance(RemoteDataSourceImpl(RetrofitHelper.apiService))
                )
            )
            HomeScreen( viewModel = viewModel)
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