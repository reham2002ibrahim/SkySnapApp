package com.example.skysnapproject.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.skysnapproject.dataLayer.local.PlaceDatabase
import com.example.skysnapproject.dataLayer.local.PlaceLocalDataSource
import com.example.skysnapproject.dataLayer.remote.RemoteDataSourceImpl
import com.example.skysnapproject.dataLayer.remote.RetrofitHelper
import com.example.skysnapproject.dataLayer.repo.Repository
import com.example.skysnapproject.favFeatsure.FavViewModel
import com.example.skysnapproject.favFeatsure.FavViewModelFactory
import com.example.skysnapproject.screens.AlarmScreen
import com.example.skysnapproject.screens.FavoriteScreen
import com.example.skysnapproject.locationFeatch.HomeScreen
import com.example.skysnapproject.locationFeatch.LocationManager
import com.example.skysnapproject.locationFeatch.WeatherViewModel
import com.example.skysnapproject.locationFeatch.WeatherViewModelFactory
import com.example.skysnapproject.screens.MapScreen
import com.example.skysnapproject.screens.SettingsScreen
import com.example.skysnapproject.screens.SplashScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "Splash"
    ) {


        composable("Splash") {
            SplashScreen(navController = navController)
        }

        composable(BottomBarRoutes.HomeRoute.title) {
            val context = LocalContext.current
            val viewModel: WeatherViewModel = viewModel(
                factory = WeatherViewModelFactory(
                    Repository.getInstance(
                        remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.apiService),
                        localDataSource = PlaceLocalDataSource(
                            dao = PlaceDatabase.getInstance(context).placeDao()
                        )
                    )
                )
            )
            HomeScreen(viewModel = viewModel)
        }


        composable("map") {
            val context = LocalContext.current
            val viewModel: WeatherViewModel = viewModel(
                factory = WeatherViewModelFactory(
                    Repository.getInstance(
                        remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.apiService),
                        localDataSource = PlaceLocalDataSource(
                            dao = PlaceDatabase.getInstance(context).placeDao()
                        )
                    )
                )
            )
            MapScreen(viewModel = viewModel)
        }


        composable(BottomBarRoutes.FavRoute.title) {
            val context = LocalContext.current
            val viewModel: FavViewModel = viewModel(
                factory = FavViewModelFactory(
                    Repository.getInstance(
                        remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.apiService),
                        localDataSource = PlaceLocalDataSource(
                            dao = PlaceDatabase.getInstance(context).placeDao()
                        )
                    )
                )
            )
            FavoriteScreen(navController = navController, viewModel = viewModel)
        }


        composable(BottomBarRoutes.AlarmRoute.title) {
            AlarmScreen()
        }
        composable(BottomBarRoutes.SettingsRoute.title) {
            SettingsScreen()
        }
    }
}