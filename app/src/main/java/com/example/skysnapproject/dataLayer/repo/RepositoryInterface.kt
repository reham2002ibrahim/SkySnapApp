package com.example.skysnapproject.dataLayer.repo

import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import retrofit2.Response

interface RepositoryInterface {

    suspend fun getCurrentWeather(city: String): Response<CurrentWeather>


}