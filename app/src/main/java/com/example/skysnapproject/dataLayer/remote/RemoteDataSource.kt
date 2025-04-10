package com.example.skysnapproject.dataLayer.remote

import android.location.Location
import com.example.skysnapproject.dataLayer.models.Nominatim
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {


    suspend fun getCurrentWeather(location : Location): Flow<CurrentWeather>

    suspend fun getForecast(location : Location): Flow<Forecast>

   suspend fun searchLocation(query: String): Flow<List<Nominatim>>

    // suspend fun searchLocation(query: String): Flow<NominatimResponse>




}