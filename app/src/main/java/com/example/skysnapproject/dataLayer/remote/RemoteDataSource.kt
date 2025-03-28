package com.example.skysnapproject.dataLayer.remote

import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import retrofit2.Response

interface RemoteDataSource {

    suspend fun getCurrentWeather(city: String): Response<CurrentWeather>

    suspend fun getForecast(city: String): Response<Forecast>


}