package com.example.skysnapproject.dataLayer.repo

import android.location.Location
import com.example.skysnapproject.dataLayer.models.Nominatim
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import com.example.skysnapproject.dataLayer.models.Alert
import com.example.skysnapproject.dataLayer.models.Place
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {

//    suspend fun getCurrentWeather(city: String): Response<CurrentWeather>
    suspend fun getCurrentWeather(location: Location): Flow<CurrentWeather>

    suspend fun getForecast(location: Location): Flow<Forecast>

    suspend fun searchLocation(query: String): Flow<List<Nominatim>>





    suspend fun getFavPlace(): Flow<List<Place>>
    suspend fun addPlace(place: Place):  Long
    suspend fun removePlace(place: Place):  Int


    suspend fun getAlerts(): Flow<List<Alert>>
    suspend fun addAlert(alert: Alert):  Long
    suspend fun removeAlert(alert: Alert):  Int


}