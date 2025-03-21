package com.example.skysnapproject.dataLayer.remote

import com.example.skysnapproject.dataLayer.model.CurrentWeather
import retrofit2.Response

interface RemoteDataSource {

    suspend fun getCurrentWeather(city: String, apiKey: String): Response<CurrentWeather>


}