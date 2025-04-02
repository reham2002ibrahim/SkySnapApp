package com.example.skysnapproject.dataLayer.remote

import com.example.skysnapproject.dataLayer.models.Nominatim
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


// API_KEY
interface ApiService {


    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double = 0.0,
        @Query("lon") lng: Double = 0.0,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): Response<CurrentWeather>


    @GET("forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double = 0.0,
        @Query("lon") lng: Double = 0.0,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): Response<Forecast>

    @GET("https://nominatim.openstreetmap.org/search")
    suspend fun searchLocation(
        @Query("q") query: String,
        @Query("format") format: String = "json"
    ): Response<List<Nominatim>>
}
