package com.example.skysnapproject.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.skysnapproject.dataLayer.local.DAO
import com.example.skysnapproject.dataLayer.local.LocalDataSource
import com.example.skysnapproject.dataLayer.local.PlaceDatabase
import com.example.skysnapproject.dataLayer.local.PlaceLocalDataSource
import com.example.skysnapproject.dataLayer.remote.RemoteDataSourceImpl
import com.example.skysnapproject.dataLayer.remote.RetrofitHelper
import com.example.skysnapproject.dataLayer.repo.Repository
import com.example.skysnapproject.screens.AlarmScreen
import com.example.skysnapproject.screens.FavoriteScreen
import com.example.skysnapproject.locationFeatch.HomeScreen
import com.example.skysnapproject.locationFeatch.LocationManager
import com.example.skysnapproject.locationFeatch.WeatherViewModel
import com.example.skysnapproject.locationFeatch.WeatherViewModelFactory
import com.example.skysnapproject.screens.MapScreen
import com.example.skysnapproject.screens.SettingsScreen
import com.example.skysnapproject.screens.SplashScreen
import kotlinx.coroutines.launch

@Composable
fun BottomNavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = "Favorites"
    ) {
        composable("Splash") {
            SplashScreen(navController = navController)
        }
        composable(BottomBarRoutes.HomeRoute.title) {
            val context = LocalContext.current
            val locationManager = remember { LocationManager(context) }
            val viewModel: WeatherViewModel = viewModel(
                factory = WeatherViewModelFactory(
                    Repository.getInstance(
                        remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.apiService),
                        localDataSource = PlaceLocalDataSource(
                            dao = PlaceDatabase.getInstance(context).placeDao()
                        )
                    ),
                    locationManager
                )
            )

            HomeScreen(viewModel = viewModel)
        }


        composable("map") {
            val context = LocalContext.current
            val locationManager = remember { LocationManager(context) }
            val repository = Repository.getInstance(
                remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.apiService),
                localDataSource = PlaceLocalDataSource(
                    dao = PlaceDatabase.getInstance(context).placeDao()
                )
            )
            MapScreen(repository = repository, locationManager = locationManager)
        }

          /*  composable("map") {
                MapScreen()
            }*/



        composable(BottomBarRoutes.FavRoute.title) {
            val context = LocalContext.current
            val repository = Repository.getInstance(
                remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.apiService),
                localDataSource = PlaceLocalDataSource(
                    dao = PlaceDatabase.getInstance(context).placeDao()
                )
            )

            FavoriteScreen(navController = navController, repository = repository)
        }
        composable(BottomBarRoutes.AlarmRoute.title) {
            AlarmScreen()
        }
        composable(BottomBarRoutes.SettingsRoute.title) {
            SettingsScreen()
        }
    }
}