package com.example.skysnapproject.locationFeatch

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skysnapproject.dataLayer.PlaceModels.Place
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import com.example.skysnapproject.dataLayer.repo.RepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class WeatherViewModel(private val repository: RepositoryInterface) : ViewModel() {


    private val _weatherState = MutableStateFlow<Response<CurrentWeather>>(Response.Loading)
    val weatherState: StateFlow<Response<CurrentWeather>> = _weatherState.asStateFlow()

    private val _forecastState = MutableStateFlow<Response<Forecast>>(Response.Loading)
    val forecastState: StateFlow<Response<Forecast>> = _forecastState.asStateFlow()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()


    fun fetchLocation(context: Context) {
        viewModelScope.launch {
            val locationManager = LocationManager(context)
            locationManager.fetchLocation()
            val city = locationManager.currentCity
            city?.let {
                getCurrentWeather(it)
                getForecast(it)
            }
        }
    }

    fun getCurrentWeather(city: String) {
        viewModelScope.launch {
            _weatherState.value = Response.Loading
            try {
                repository.getCurrentWeather(city)
                    .collect { weatherData ->
                        _weatherState.value = Response.Success(weatherData)
                    }
            } catch (e: Exception) {
                _weatherState.value = Response.Failure(e)
            }
        }
    }


    //  ******* forecast data
   /* private val _forecastState = MutableStateFlow<ForecastState>(ForecastState.Empty)
    val forecastState: StateFlow<ForecastState> = _forecastState*/

    fun getForecast(city: String) {
        viewModelScope.launch {
            _forecastState.value = Response.Loading
            try {
                repository.getForecast(city)
                    .collect { forecastData ->
                        _forecastState.value = Response.Success(forecastData)
                    }
            } catch (e: Exception) {
                _forecastState.value = Response.Failure(e)
            }
        }
    }


    // for saving location

    fun saveLocation(place: Place) {
        viewModelScope.launch {
            val ans = repository.addPlace(place)
            if (ans > 0) Log.i("TAG", "saveLocation: added sussefully  ")
            else Log.i("TAG", "saveLocation: can't  added  ")
        }
    }



    sealed class Response<out T> {
        object Loading : Response<Nothing>()
        data class Success<T>(val data: T) : Response<T>()
        data class Failure(val error: Throwable) : Response<Nothing>()
    }



    class WeatherViewModelFactory(
        private val repository: RepositoryInterface
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WeatherViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }




}