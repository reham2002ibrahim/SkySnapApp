package com.example.skysnapproject.locationFeatch

import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.skysnapproject.R
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import com.example.skysnapproject.dataLayer.forecastModel.ForecastItem
import com.example.skysnapproject.screens.GradientBackground
import com.example.skysnapproject.screens.LoaderAnimation
import com.example.skysnapproject.utils.getPreference
import java.util.Calendar
import java.util.Date
import java.util.Locale
import android.Manifest
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.skysnapproject.dataLayer.models.Place
import com.example.skysnapproject.utils.getPlaceFromSharedPreferences
import com.example.skysnapproject.utils.getSharedPrefForHome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun HomeScreen(navController: NavController, viewModel: WeatherViewModel) {
    val context = LocalContext.current

    val weatherState by viewModel.weatherState.collectAsStateWithLifecycle()
    val forecastState by viewModel.forecastState.collectAsStateWithLifecycle()
    val permissionState by viewModel.permissionState.collectAsStateWithLifecycle()


      val locationPreference = getPreference(context, "location", "GPS")
      var place by remember { mutableStateOf<Place?>(null) }
      var isLocationSelected by remember { mutableStateOf(false) }

      val mapResult = navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("MAP_RESULT") ?: false

    LaunchedEffect(locationPreference, isLocationSelected, mapResult) {
              if (locationPreference == "GPS") {
                  viewModel.requestLocationPermission(context)
              } else {
                  viewModel.viewModelScope.launch(Dispatchers.IO) {
                      if (!isLocationSelected || mapResult) {
                          val savedPlace = getSharedPrefForHome(context)
                          place = savedPlace
                          isLocationSelected = savedPlace?.lat != 0.0 && savedPlace?.lng != 0.0

                          if (isLocationSelected) {
                              val location = Location("").apply {
                                  latitude = savedPlace?.lat ?: 0.0
                                  longitude = savedPlace?.lng ?: 0.0
                              }
                              viewModel.getCurrentWeather(location)
                              viewModel.getForecast(location)
                          } else {
                              withContext(Dispatchers.Main) {
                                  navController.previousBackStackEntry?.savedStateHandle?.set("origin", "HomeScreen")
                                  navController.navigate("homeMap")
                              }
                          }
                      } else {
                          val savedLocation = Location("").apply {
                              latitude = place?.lat ?: 0.0
                              longitude = place?.lng ?: 0.0
                          }
                          viewModel.getCurrentWeather(savedLocation)
                          viewModel.getForecast(savedLocation)
                      }

                      if (mapResult) {
                          withContext(Dispatchers.Main) {
                              navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("MAP_RESULT")
                          }
                      }
                  }
              }
          }


    if (locationPreference == "GPS" && !permissionState) {
        RequestLocationPermission(viewModel = viewModel)
    }


    GradientBackground()


    when (val state = weatherState) {
        is WeatherViewModel.Response.Loading -> LoadingScreen()
        is WeatherViewModel.Response.Success -> {
            val weather = state.data as? CurrentWeather
            val forecast = (forecastState as? WeatherViewModel.Response.Success)?.data as? Forecast

            if (weather != null) {
                WeatherContent(weather, forecast)
            }
        }
        is WeatherViewModel.Response.Failure -> {
            ErrorScreen(message = state.error.message ?: "Unknown error")
        }
        else -> LoadingScreen()
    }
}

@Composable
fun RequestLocationPermission(viewModel: WeatherViewModel) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            viewModel.setPermissionGranted(granted, context)
        }
    )
    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LoaderAnimation(
            modifier = Modifier.fillMaxSize(),
            anmi = R.raw.loading
        )
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
fun WeatherContent(weather: CurrentWeather, forecast: Forecast?) {
    val currentDate =
        remember { SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(Date()) }
    val currentTime = remember { SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date()) }
    val weatherIcon =
        "https://openweathermap.org/img/wn/${weather.weather.firstOrNull()?.icon}@2x.png"
    /*
        LazyColumn(

          modifier = Modifier
                .fillMaxHeight()
                .padding(top = 70.dp, start = 10.dp, end = 10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )*/

    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(top = 70.dp, start = 10.dp, end = 10.dp),


            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )

        {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "LocationIcon",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = weather.name, fontSize = 26.sp, color = Color.White)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = weather.sys?.country ?: "", fontSize = 16.sp, color = Color.Gray)
                }
            }

            item {
                var context = LocalContext.current
                val unit = getPreference(context, "units", "Celsius")
                val unitSymbol = when (unit) {
                    "Celsius" -> "°C"
                    "Fahrenheit" -> "°F"
                    "Kelvin" -> "K"
                    else -> "°C"
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${weather.main.temp.toInt()}$unitSymbol", fontSize = 50.sp,
                        fontWeight = FontWeight.Bold, color = Color.White
                    )
                    GlideImage(
                        model = weatherIcon,
                        contentDescription = "Weather icon",
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = weather.weather.firstOrNull()?.description ?: "",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.Center) {
                    Text(text = currentDate, fontSize = 20.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(40.dp))
                    Text(text = currentTime, fontSize = 20.sp, color = Color.White)
                }
            }

            item { WeatherDetails(weather) }

            item {
                Text(
                    text = "Hourly Forecast",
                    fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White
                )
            }

            item {
                forecast?.let {
                    HourlyForecast(it.list.take(8))
                } ?: run {
                    Text("Loading forecast...", color = Color.LightGray)
                }
            }

            item {
                Text(
                    text = "Next 5 Days",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            items(
                getDailyForecast(forecast?.list ?: emptyList()),
                key = { it.fullDate.time }) { daily ->
                DailyForecastItem(daily)
            }

        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourlyForecast(hourlyData: List<ForecastItem>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(hourlyData) { item ->
            HourlyForecastItem(item)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourlyForecastItem(item: ForecastItem) {
    val timeFormat = SimpleDateFormat("h a", Locale.getDefault())
    val time = remember { timeFormat.format(Date(item.dt * 1000L)) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, Color.White, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = time, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            GlideImage(
                model = "https://openweathermap.org/img/wn/${item.weather.firstOrNull()?.icon}@2x.png",
                contentDescription = "Weather icon",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "${item.main.temp.toInt()}°C", color = Color.White)
        }
    }
}

@Composable
fun WeatherDetails(weather: CurrentWeather) {
    var  context = LocalContext.current
    fun windSpeedUnit(speed: Double): String {
        val unitPreference = getPreference(context, "wind_speed_unit", "m/s")
        return when (unitPreference) {
            "mile/hour" -> String.format("%.3f mph", speed * 2.237)
            else -> String.format("%.3f m/s", speed)
        }
    }

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
                WeatherData("    Humidity", "${weather.main.humidity}%")
                WeatherData("Wind Speed", "${windSpeedUnit(weather.wind.speed)}")
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
    Column(
        modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.LightGray)
    }
}


data class DailyForecast(
    val date: String,
    val fullDate: Date,
    val tempMin: Int,
    val tempMax: Int,
    val icon: String,
    val description: String
)


fun getDailyForecast(list: List<ForecastItem>): List<DailyForecast> {
    if (list.isEmpty()) return emptyList()

    val dailyMap = mutableMapOf<String, MutableList<ForecastItem>>()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())

    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val todayMidnight = calendar.timeInMillis / 1000

    list.forEach { item ->
        val itemDate = Date(item.dt * 1000L)
        val date = dateFormat.format(itemDate)

        if (item.dt >= todayMidnight) {
            dailyMap.getOrPut(date) { mutableListOf() }.add(item)
        }
    }
    val fixedDays = mutableListOf<DailyForecast>()
    repeat(5) { _ ->
        val currentDate = calendar.time
        val currentDateString = dateFormat.format(currentDate)

        val items = dailyMap[currentDateString] ?: mutableListOf()

        val noonItem = items.firstOrNull {
            SimpleDateFormat("H", Locale.getDefault()).format(Date(it.dt * 1000L)) == "12"
        } ?: items.lastOrNull()

        fixedDays.add(
            DailyForecast(
                date = dayFormat.format(currentDate),
                fullDate = currentDate,
                tempMin = items.minOfOrNull { it.main.temp_min.toInt() }
                    ?: 10,
                tempMax = items.maxOfOrNull { it.main.temp_max.toInt() }
                    ?: 20,
                icon = noonItem?.weather?.firstOrNull()?.icon ?: "01d",
                description = noonItem?.weather?.firstOrNull()?.description ?: "No Data"
            )
        )
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    fixedDays.forEach { forecast ->
        println("${forecast.date} - ${forecast.fullDate} | Min: ${forecast.tempMin}, Max: ${forecast.tempMax}")
    }
    return fixedDays
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DailyForecastItem(daily: DailyForecast) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = daily.date, fontSize = 16.sp, fontWeight = FontWeight.Bold,
                color = Color.White, modifier = Modifier.weight(1f)
            )

            GlideImage(
                model = "https://openweathermap.org/img/wn/${daily.icon}@2x.png",
                contentDescription = "Weather icon",
                modifier = Modifier
                    .size(40.dp)
                    .weight(1f)
            )

            Text(
                text = daily.description.replaceFirstChar { it.uppercase() }, fontSize = 14.sp,
                color = Color.White, modifier = Modifier.weight(2f)
            )

            Text(
                text = "${daily.tempMin}°/${daily.tempMax}°",
                fontSize = 14.sp,
                color = Color.LightGray,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
