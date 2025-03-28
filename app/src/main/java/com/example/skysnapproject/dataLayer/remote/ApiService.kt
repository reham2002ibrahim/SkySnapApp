package com.example.skysnapproject.dataLayer.remote

import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


// API_KEY
interface ApiService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query ("q") city : String ,
        @Query ("units") units : String = "metric" ,
        @Query ("appid") apiKey : String
    ) :Response<CurrentWeather>


    @GET("forecast")
    suspend fun getForecast(
        @Query ("q") city : String ,
        @Query ("units") units : String = "metric" ,
        @Query ("appid") apiKey : String
    ) :Response<Forecast>




}