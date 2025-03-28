package com.example.skysnapproject.locationFeatch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import com.example.skysnapproject.dataLayer.repo.RepositoryInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: RepositoryInterface) : ViewModel() {
    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Empty)
    val weatherState: StateFlow<WeatherState> = _weatherState

    fun getCurrentWeather(city: String) {
        viewModelScope.launch {
            _weatherState.value = WeatherState.Loading
            try {
                val response = repository.getCurrentWeather(city)
                if (response.isSuccessful && response.body() != null) {
                    _weatherState.value = WeatherState.Success(response.body()!!)
                } else {
                    _weatherState.value = WeatherState.Error("Failed to get weather data")
                }
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error(e.message ?: "chached error")
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
                val response = repository.getForecast(city)
                if (response.isSuccessful && response.body() != null) {
                    _forecastState.value = ForecastState.Success(response.body()!!)
                } else {
                    _forecastState.value = ForecastState.Error("Failed to get forecast")
                }
            } catch (e: Exception) {
                _forecastState.value = ForecastState.Error(e.message ?: "chached error")
            }
        }
    }

    sealed class ForecastState {
        object Empty : ForecastState()
        object Loading : ForecastState()
        data class Success(val forecastData: Forecast) : ForecastState()
        data class Error(val message: String) : ForecastState()
    }
}