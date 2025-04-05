package com.example.skysnapproject.homeFeature

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skysnapproject.dataLayer.models.Nominatim
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import com.example.skysnapproject.dataLayer.models.Alert
import com.example.skysnapproject.dataLayer.repo.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


class WeatherViewModel(private val repository: RepositoryInterface) : ViewModel() {


    private val _weatherState = MutableStateFlow<Response<CurrentWeather>>(Response.Loading)
    val weatherState: StateFlow<Response<CurrentWeather>> = _weatherState.asStateFlow()

    private val _forecastState = MutableStateFlow<Response<Forecast>>(Response.Loading)
    val forecastState: StateFlow<Response<Forecast>> = _forecastState.asStateFlow()


    private val _searchLocationState = MutableSharedFlow<Response<List<Nominatim>>>()
    val searchLocationState: SharedFlow<Response<List<Nominatim>>> = _searchLocationState.asSharedFlow()

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _permissionState = MutableStateFlow(false)
    val permissionState: StateFlow<Boolean> get() = _permissionState.asStateFlow()




fun requestLocationPermission(context: Context) {
    if (ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        _permissionState.value = true
        weatherWithGPS(context)
    } else {
        _permissionState.value = false
    }
}

    fun setPermissionGranted(granted: Boolean , context: Context) {
        _permissionState.value = granted
        if (granted) {
            weatherWithGPS(context)
        }
    }





    fun weatherWithGPS(context: Context) {
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


    //  forecast data

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

/*    fun saveLocation(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            val ans = repository.addPlace(place)
            if (ans > 0) Log.i("TAG", "saveLocation: added sussefully  ")
            else Log.i("TAG", "saveLocation: can't  added  ")
        }
    }*/

    fun saveAlert(alert: Alert) {
        viewModelScope.launch(Dispatchers.IO) {
            val ans = repository.addAlert(alert)
            if (ans > 0) Log.i("TAG", "saveAlert: added sussefully  ")
            else Log.i("TAG", "saveAlert: can't  added  ")
        }
    }

    private val _alerts = MutableStateFlow<List<Alert>>(emptyList())
    val allAlerts: StateFlow<List<Alert>> = _alerts.asStateFlow()
    init {
        loadAlerts()
    }

    private fun loadAlerts() {
        viewModelScope.launch (Dispatchers.IO){
            repository.getAlerts().collect { alerts ->
                _alerts.emit(alerts)
            }
        }
    }
    fun deleteAlert(alert: Alert) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.removeAlert(alert)
                withContext(Dispatchers.Main) {
                    if (result > 0) {
                        _message.emit("Deleted successfully")
                        Log.i("DeleteAlert", "deletAlert: deleted sussefully ")

                    } else {
                        _message.emit("not found product")
                        Log.i("DeleteAlert", "deletAlert: cant' delete ")
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    _message.emit("Error: ${ex.message}")
                }
            }

        }
    }




    sealed class Response<out T> {
        object Loading : Response<Nothing>()
        data class Success<T>(val data: T) : Response<T>()
        data class Failure(val error: Throwable) : Response<Nothing>()
    }


    // search location
    fun getSearchLocation(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchLocationState.emit(Response.Loading)
            try {
                repository.searchLocation(query)
                    .collect { searchResults ->
                        withContext(Dispatchers.Main) {
                            _searchLocationState.emit(Response.Success(searchResults))
                        }
                    }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _searchLocationState.emit(Response.Failure(e))
                }
            }
        }
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




    // for alarm


}