package com.example.skysnapproject.dataLayer.remote

import com.example.skysnapproject.dataLayer.model.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET("weather")
    suspend fun getCurrentWeather(
        @Query ("q") city : String ,
        @Query ("units") units : String = "metric" ,
        @Query ("appid") apiKey : String
    ) :Response<CurrentWeather>




}