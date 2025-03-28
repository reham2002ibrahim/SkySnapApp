package com.example.skysnapproject.locationFeatch

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
//import com.bumptech.glide.integration.compose.GlideImage
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.screens.GradientBackground
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreen(viewModel: WeatherViewModel) {


    val context = LocalContext.current
    val locationManager = remember { LocationManager(context) }
    val weatherState by viewModel.weatherState.collectAsState()

    LaunchedEffect(Unit) {
        locationManager.fetchLocation()
        locationManager.currentCity?.let { city ->
            viewModel.getCurrentWeather(city)
        }
    }


    GradientBackground()

    when (weatherState) {
        is WeatherViewModel.WeatherState.Loading -> LoadingScreen()
        is WeatherViewModel.WeatherState.Success -> {
            val weather = (weatherState as WeatherViewModel.WeatherState.Success).weatherData
            WeatherContent(weather)
        }

        is WeatherViewModel.WeatherState.Error -> {
            ErrorScreen(message = (weatherState as WeatherViewModel.WeatherState.Error).message)
        }

        WeatherViewModel.WeatherState.Empty -> {}
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = Color.Red, fontSize = 18.sp)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeatherContent(weather: CurrentWeather) {


    val currentDate =
        remember { SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(Date()) }
    val currentTime = remember { SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date()) }

    val weatherIcon =
        "https://openweathermap.org/img/wn/${weather.weather.firstOrNull()?.icon}@2x.png"


    Log.i("TAG WeatherContent", "WeatherContent:  ${weatherIcon}")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 70.dp, start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "LocationIcon",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = weather.name, fontSize = 26.sp, color = Color.White)
            }
        }

        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${weather.main.temp.toInt()}°C",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                GlideImage(
                    model = weatherIcon,
                    contentDescription = "Weather icon",
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = weather.weather.firstOrNull()?.description ?: "",
                    fontSize = 20.sp,
                    color = Color.LightGray
                )
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.Center) {
                Text(text = currentDate, fontSize = 16.sp, color = Color.LightGray)
                Spacer(modifier = Modifier.width(40.dp))
                Text(text = currentTime, fontSize = 16.sp, color = Color.Gray)
            }
        }

        item {
            WeatherDetails(weather)
        }
    }
}

@Composable
fun WeatherDetails(weather: CurrentWeather) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
            .border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherData("Humidity", "${weather.main.humidity}%")
                WeatherData("Wind Speed", "${weather.wind.speed} m/s")
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherData("Pressure", "${weather.main.pressure} hPa")
                WeatherData("Clouds", "${weather.clouds.all}%")
            }
        }
    }
}

@Composable
fun WeatherData(key: String, value: String) {
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.LightGray)
    }
}
