package com.example.skysnapproject.locationFeatch

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skysnapproject.dataLayer.PlaceModels.Place
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import com.example.skysnapproject.dataLayer.repo.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WeatherViewModel(private val repository: RepositoryInterface) : ViewModel() {


    private val _weatherState = MutableStateFlow<Response<CurrentWeather>>(Response.Loading)
    val weatherState: StateFlow<Response<CurrentWeather>> = _weatherState.asStateFlow()

    private val _forecastState = MutableStateFlow<Response<Forecast>>(Response.Loading)
    val forecastState: StateFlow<Response<Forecast>> = _forecastState.asStateFlow()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()


    /*  fun fetchLocation(context: Context) {
          viewModelScope.launch(Dispatchers.IO) {
              val locationManager = LocationManager(context)
             locationManager.fetchLocation()
              val city = locationManager.currentCity
              city?.let {
                  withContext(Dispatchers.Main) {
                      getCurrentWeather(it)
                      getForecast(it)
                  }
              }
          }
      }*/
    fun fetchLocation(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val locationManager = LocationManager(context)
            val location = locationManager.fetchLocation()
            withContext(Dispatchers.Main) {
                location?.let { getCurrentWeather(it) }
                location?.let { getForecast(it) }
            }
        }
    }


    fun getCurrentWeather(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            _weatherState.value = Response.Loading
            try {
                repository.getCurrentWeather(location)
                    .collect { weatherData ->

                        withContext(Dispatchers.Main) {
                            _weatherState.value = Response.Success(weatherData)
                        }
                    }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _weatherState.value = Response.Failure(e)
                }
            }
        }
    }


    //  ******* forecast data

    fun getForecast(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            _forecastState.value = Response.Loading
            try {
                repository.getForecast(location)
                    .collect { forecastData ->
                        withContext(Dispatchers.Main) {
                            _forecastState.value = Response.Success(forecastData)
                        }
                    }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _forecastState.value = Response.Failure(e)
                }
            }
        }
    }


    // for saving location

    fun saveLocation(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
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