package com.example.skysnapproject.dataLayer.remote

import android.content.Context
import android.location.Location
import com.example.skysnapproject.BuildConfig.API_KEY
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.skysnapproject.dataLayer.models.Nominatim
import com.example.skysnapproject.utils.getPreference


class RemoteDataSourceImpl(private val apiService: ApiService, private val context: Context) : RemoteDataSource {

    override suspend fun getCurrentWeather(location: Location): Flow<CurrentWeather> {
        return flow {
            val unit = when (getPreference(context, "units", "Celsius")) {
                "Celsius" -> "metric"
                "Fahrenheit" -> "imperial"
                "Kelvin" -> ""
                else -> "metric"
            }


            val response = apiService.getCurrentWeather(location.latitude, location.longitude, unit, API_KEY)
            if (response.isSuccessful && response.body() != null) {
                emit(response.body()!!)
            } else {
                throw Exception("Failed to get weather data")
            }
        }
    }

    override suspend fun getForecast(location: Location): Flow<Forecast> {
        return flow {
            val unit = when (getPreference(context, "units", "Celsius")) {
                "Celsius" -> "metric"
                "Fahrenheit" -> "imperial"
                "Kelvin" -> ""
                else -> "metric"
            }
            val response = apiService.getForecast(location.latitude, location.longitude,unit, API_KEY)
            if (response.isSuccessful && response.body() != null) {
                emit(response.body()!!)
            } else {
                throw Exception("Failed to get forecast data")
            }
        }
    }

    override suspend fun searchLocation(query: String): Flow<List<Nominatim>> {
        return flow {
            val response = apiService.searchLocation(query, "json")
            if (response.isSuccessful && response.body() != null) {
                emit(response.body()!!)
            } else {
                throw Exception("Failed to get search results")
            }
        }
    }


}






