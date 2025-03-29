package com.example.skysnapproject.dataLayer.remote

import com.example.skysnapproject.BuildConfig.API_KEY
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class RemoteDataSourceImpl(private val apiService: ApiService) : RemoteDataSource {

    override suspend fun getCurrentWeather(city: String): Flow<CurrentWeather> {
        return flow {
            val response = apiService.getCurrentWeather(city, "metric", API_KEY)
            if (response.isSuccessful && response.body() != null) {
                emit(response.body()!!)
            } else {
                throw Exception("Failed to get weather data")
            }
        }
    }

    override suspend fun getForecast(city: String): Flow<Forecast> {
        return flow {
            val response = apiService.getForecast(city, "metric", API_KEY)
            if (response.isSuccessful && response.body() != null) {
                emit(response.body()!!)
            } else {
                throw Exception("Failed to get forecast data")
            }
        }
    }


}
