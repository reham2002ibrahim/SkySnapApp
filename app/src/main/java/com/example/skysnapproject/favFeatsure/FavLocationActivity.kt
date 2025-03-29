package com.example.skysnapproject.favFeatsure

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.example.skysnapproject.locationFeatch.WeatherViewModel

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skysnapproject.dataLayer.local.PlaceDatabase
import com.example.skysnapproject.dataLayer.local.PlaceLocalDataSource
import com.example.skysnapproject.dataLayer.remote.RemoteDataSourceImpl
import com.example.skysnapproject.dataLayer.remote.RetrofitHelper
import com.example.skysnapproject.dataLayer.repo.Repository
import com.example.skysnapproject.locationFeatch.ErrorScreen
import com.example.skysnapproject.locationFeatch.LoadingScreen
import com.example.skysnapproject.locationFeatch.WeatherContent
import com.example.skysnapproject.locationFeatch.WeatherViewModelFactory
import com.example.skysnapproject.screens.GradientBackground
import com.example.skysnapproject.ui.theme.SkySnapProjectTheme


class FavLocationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cityName = intent.getStringExtra("CITY_NAME") ?: ""

        setContent {
            SkySnapProjectTheme {
                FavLocationScreen(cityName = cityName)
            }
        }
    }
}


@SuppressLint("ContextCastToActivity")
@Composable
fun FavLocationScreen(cityName: String) {
    val context = LocalContext.current


    val viewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory(
            repository = Repository.getInstance(
                remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.apiService),
                localDataSource = PlaceLocalDataSource(
                    dao = PlaceDatabase.getInstance(LocalContext.current).placeDao()
                )
            )
        )
    )

    LaunchedEffect(cityName) {
        if (cityName.isNotEmpty()) {
            viewModel.getCurrentWeather(cityName)
            viewModel.getForecast(cityName)
        }
    }

    val weatherState by viewModel.weatherState.collectAsState()
    val forecastState by viewModel.forecastState.collectAsState()

    GradientBackground()
    fun Context.findActivity(): Activity? {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        return null
    }

    when {
        weatherState is WeatherViewModel.WeatherState.Loading -> LoadingScreen()
        weatherState is WeatherViewModel.WeatherState.Success -> {
            val weather = (weatherState as WeatherViewModel.WeatherState.Success).weatherData
            val forecast = (forecastState as? WeatherViewModel.ForecastState.Success)?.forecastData

            Column(modifier = Modifier.fillMaxSize()) {
                IconButton(
                    onClick = {
                        when (val activity = context.findActivity()) {
                            null -> Log.e("Navigation", "Context is not an Activity")
                            else -> activity.finish()
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                WeatherContent(weather, forecast)
            }
        }
        weatherState is WeatherViewModel.WeatherState.Error -> {
            ErrorScreen(message = (weatherState as WeatherViewModel.WeatherState.Error).message)
        }
        else -> LoadingScreen()
    }
}
