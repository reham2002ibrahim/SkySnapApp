package com.example.skysnapproject.dataLayer.repo

import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import com.example.skysnapproject.dataLayer.PlaceModels.Place
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepositoryInterface {

//    suspend fun getCurrentWeather(city: String): Response<CurrentWeather>
    suspend fun getCurrentWeather(city: String): Flow<CurrentWeather>

    suspend fun getForecast(city: String): Flow<Forecast>




    suspend fun getFavPlace(): Flow<List<Place>>
    suspend fun addPlace(place: Place):Flow<  Long>
    suspend fun removePlace(place: Place): Flow< Int>


}