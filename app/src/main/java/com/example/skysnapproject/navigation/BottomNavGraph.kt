package com.example.skysnapproject.navigation

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.skysnapproject.alertFeature.AlarmScreen
import com.example.skysnapproject.favFeatsure.FavoriteScreen
import com.example.skysnapproject.homeFeature.HomeScreen
import com.example.skysnapproject.homeFeature.WeatherViewModel
import com.example.skysnapproject.homeFeature.HomeMap
import com.example.skysnapproject.alertFeature.MapOfAlert
import com.example.skysnapproject.favFeatsure.MapScreen
import com.example.skysnapproject.screens.SettingsScreen
import com.example.skysnapproject.screens.SplashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "Splash"
    ) {


        composable("Splash") {
            SplashScreen(navController = navController)
        }

        composable(BottomBarRoutes.HomeRoute.route) {
            val context = LocalContext.current
            val viewModel: WeatherViewModel = viewModel(
                factory = WeatherViewModel.WeatherViewModelFactory(
                    Repository.getInstance(
                        remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.apiService, context),
                        localDataSource = PlaceLocalDataSource(
                            dao = PlaceDatabase.getInstance(context).placeDao()
                        )
                    )
                )
            )
            HomeScreen(navController,viewModel = viewModel)
        }


        composable("map") {
            val context = LocalContext.current
            val viewModel: WeatherViewModel = viewModel(
                factory = WeatherViewModel.WeatherViewModelFactory(
                    Repository.getInstance(
                        remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.apiService, context),
                        localDataSource = PlaceLocalDataSource(
                            dao = PlaceDatabase.getInstance(context).placeDao()
                        )
                    )
                )
            )
            val favViewModel: FavViewModel = viewModel(
                factory = FavViewModel.FavViewModelFactory(
                    Repository.getInstance(
                        remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.apiService, context),
                        localDataSource = PlaceLocalDataSource(
                            dao = PlaceDatabase.getInstance(context).placeDao()
                        )
                    )
                )
            )
            MapScreen(viewModel = viewModel, favViewModel)
        }
        composable("alertMap") {
            val context = LocalContext.current
            val viewModel: WeatherViewModel = viewModel(
                factory = WeatherViewModel.WeatherViewModelFactory(
                    Repository.getInstance(
                        remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.apiService ,context),
                        localDataSource = PlaceLocalDataSource(
                            dao = PlaceDatabase.getInstance(context).placeDao()
                        )
                    )
                )
            )
            MapOfAlert(viewModel = viewModel, navController)
        }
        composable("homeMap") {
            val context = LocalContext.current
            val viewModel: WeatherViewModel = viewModel(
                factory = WeatherViewModel.WeatherViewModelFactory(
                    Repository.getInstance(
                        remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.apiService ,context),
                        localDataSource = PlaceLocalDataSource(
                            dao = PlaceDatabase.getInstance(context).placeDao()
                        )
                    )
                )
            )
            HomeMap(viewModel = viewModel, navController)
        }


        composable(BottomBarRoutes.FavRoute.route) {
            val context = LocalContext.current
            val viewModel: FavViewModel = viewModel(
                factory = FavViewModel.FavViewModelFactory(
                    Repository.getInstance(
                        remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.apiService, context),
                        localDataSource = PlaceLocalDataSource(
                            dao = PlaceDatabase.getInstance(context).placeDao()
                        )
                    )
                )
            )
            FavoriteScreen(navController = navController, viewModel = viewModel)
        }


        composable(BottomBarRoutes.AlarmRoute.route) {
            val context = LocalContext.current
            val viewModel: WeatherViewModel = viewModel(
                factory = WeatherViewModel.WeatherViewModelFactory(
                    Repository.getInstance(
                        remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.apiService, context),
                        localDataSource = PlaceLocalDataSource(
                            dao = PlaceDatabase.getInstance(context).placeDao()
                        )
                    )
                )
            )
            AlarmScreen(navController = navController,viewModel = viewModel)
        }
        composable(BottomBarRoutes.SettingsRoute.route) {
            SettingsScreen()
        }
    }
}