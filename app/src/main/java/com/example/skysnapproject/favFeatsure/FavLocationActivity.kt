package com.example.skysnapproject.favFeatsure

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.location.Location
import com.example.skysnapproject.homeFeature.WeatherViewModel
import androidx.compose.foundation.layout.statusBarsPadding

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skysnapproject.dataLayer.models.Place
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.local.PlaceDatabase
import com.example.skysnapproject.dataLayer.local.PlaceLocalDataSource
import com.example.skysnapproject.dataLayer.remote.RemoteDataSourceImpl
import com.example.skysnapproject.dataLayer.remote.RetrofitHelper
import com.example.skysnapproject.dataLayer.repo.Repository
import com.example.skysnapproject.homeFeature.ErrorScreen
import com.example.skysnapproject.homeFeature.LoadingScreen
import com.example.skysnapproject.homeFeature.WeatherContent
import com.example.skysnapproject.homeFeature.WeatherViewModel.Response
import com.example.skysnapproject.screens.GradientBackground
import com.example.skysnapproject.ui.theme.SkySnapProjectTheme


class FavLocationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val place = intent.getSerializableExtra("PLACE") as? Place
        setContent {
            SkySnapProjectTheme {
                place?.let { FavLocationScreen(it) }
            }
        }
    }
}



@SuppressLint("ContextCastToActivity")
@Composable
fun FavLocationScreen(place: Place) {
    val context = LocalContext.current


    val viewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModel.WeatherViewModelFactory(
            repository = Repository.getInstance(
                remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.apiService, context),
                localDataSource = PlaceLocalDataSource(
                    dao = PlaceDatabase.getInstance(LocalContext.current).placeDao()
                )
            )
        )
    )

    LaunchedEffect(place) {
        place?.let {
            val location = Location("").apply {
                latitude = it.lat
                longitude = it.lng
            }
            viewModel.getCurrentWeather(location)
            viewModel.getForecast(location)
        }
    }

    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()
    val forecastState by viewModel.forecastState.collectAsStateWithLifecycle()

    GradientBackground()
    fun Context.findActivity(): Activity? {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        return null
    }

    when (weatherState) {
        is Response.Loading -> LoadingScreen()

        is Response.Success -> {
            val weather = (weatherState as Response.Success<CurrentWeather>).data
            val forecast = (forecastState as? Response.Success)?.data

            Column(modifier = Modifier.fillMaxSize() .statusBarsPadding()

            ) {
                IconButton(
                    onClick = {
                        when (val activity = context.findActivity()) {
                            null -> Log.e("Navigation", "Context is not an Activity")
                            else -> activity.finish()
                        }
                    },
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

        is Response.Failure -> {
            ErrorScreen(message = (weatherState as Response.Failure).error.message ?: "Unknown Error")
        }

        else -> LoadingScreen()
    }

}
