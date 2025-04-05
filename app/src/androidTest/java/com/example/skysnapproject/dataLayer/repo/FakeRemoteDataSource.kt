package com.example.skysnapproject.dataLayer.repo

import android.location.Location
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import com.example.skysnapproject.dataLayer.models.Nominatim
import com.example.skysnapproject.dataLayer.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRemoteDataSource : RemoteDataSource {

    private val fakeLocations = listOf(
        Nominatim(lat = "123.123", lon = "321.321", display_name = "Cairo, Egypt"),
        Nominatim(lat = "456.456", lon = "654.654", display_name = "Giza, Egypt")
    )

    override suspend fun getCurrentWeather(location: Location): Flow<CurrentWeather> {
        TODO("Not yet implemented")

    }

    override suspend fun getForecast(location: Location): Flow<Forecast> {
        TODO("Not yet implemented")

    }
    override suspend fun searchLocation(query: String): Flow<List<Nominatim>> {

        return flowOf(fakeLocations)
    }

}