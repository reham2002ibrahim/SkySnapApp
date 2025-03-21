package com.example.skysnapproject.dataLayer.remote

import com.example.skysnapproject.dataLayer.model.CurrentWeather
import retrofit2.Response

class RemoteDataSourceImpl(private val apiService: ApiService) : RemoteDataSource {


    override suspend fun getCurrentWeather(city: String, apiKey: String): Response<CurrentWeather> {
        return apiService.getCurrentWeather(city, apiKey = apiKey)
    }

}
