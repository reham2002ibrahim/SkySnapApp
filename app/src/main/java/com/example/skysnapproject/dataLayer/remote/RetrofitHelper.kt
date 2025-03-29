package com.example.skysnapproject.dataLayer.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val BASE_URL= "https://api.openweathermap.org/data/2.5/"
   private val myRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = myRetrofit.create(ApiService::class.java)
}