package com.example.skysnapproject.dataLayer.repo

import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import com.example.skysnapproject.dataLayer.remote.RemoteDataSource
import retrofit2.Response


class Repository private constructor(
    private val remoteDataSource: RemoteDataSource
) : RepositoryInterface {

    override suspend fun getCurrentWeather(city: String): Response<CurrentWeather> {
        return remoteDataSource.getCurrentWeather(city)
    }

    override suspend fun getForecast(city: String): Response<Forecast> {
        return remoteDataSource.getForecast(city)
    }

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(remoteDataSource: RemoteDataSource): Repository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repository(remoteDataSource).also { INSTANCE = it }
            }
        }
    }
}
