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
import kotlinx.coroutines.launch


class WeatherViewModel(private val repository: RepositoryInterface) : ViewModel() {


    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Empty)
    val weatherState: StateFlow<WeatherState> = _weatherState


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
            _weatherState.value = WeatherState.Loading
            try {
                repository.getCurrentWeather(city)
                    .collect { weatherData ->
                        _weatherState.value = WeatherState.Success(weatherData)
                    }
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error(e.message ?: "Cached error")
            }
        }
    }




    sealed class WeatherState {
        object Empty : WeatherState()
        object Loading : WeatherState()
        data class Success(val weatherData: CurrentWeather) : WeatherState()
        data class Error(val message: String) : WeatherState()
    }

    //  ******* forecast data
    private val _forecastState = MutableStateFlow<ForecastState>(ForecastState.Empty)
    val forecastState: StateFlow<ForecastState> = _forecastState

    fun getForecast(city: String) {
        viewModelScope.launch {
            _forecastState.value = ForecastState.Loading
            try {
                repository.getForecast(city)
                    .collect { forecastData ->
                        _forecastState.value = ForecastState.Success(forecastData)
                    }
            } catch (e: Exception) {
                _forecastState.value = ForecastState.Error(e.message ?: "Cached error")
            }
        }
    }

    sealed class ForecastState {
        object Empty : ForecastState()
        object Loading : ForecastState()
        data class Success(val forecastData: Forecast) : ForecastState()
        data class Error(val message: String) : ForecastState()
    }


    // for saving location

    fun saveLocation(place: Place) {
        viewModelScope.launch {
            val ans = repository.addPlace(place)
            if (ans > 0) Log.i("TAG", "saveLocation: added sussefully  ")
            else Log.i("TAG", "saveLocation: can't  added  ")
        }
    }



}