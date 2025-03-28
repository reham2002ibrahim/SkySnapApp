package com.example.skysnapproject.dataLayer.remote

import com.example.skysnapproject.BuildConfig.API_KEY
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import retrofit2.Response

class RemoteDataSourceImpl(private val apiService: ApiService) : RemoteDataSource {


    override suspend fun getCurrentWeather(city: String): Response<CurrentWeather> {
        return  apiService.getCurrentWeather(city,"metric" , API_KEY)

    }

}
