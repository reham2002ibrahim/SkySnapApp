package com.example.skysnapproject.dataLayer.repo

import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import com.example.skysnapproject.dataLayer.PlaceModels.Place
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepositoryInterface {

    suspend fun getCurrentWeather(city: String): Response<CurrentWeather>

    suspend fun getForecast(city: String): Response<Forecast>



    suspend fun getFavPlace(): Flow<List<Place>>
    suspend fun addPlace(place: Place): Long
    suspend fun removePlace(place: Place): Int


}