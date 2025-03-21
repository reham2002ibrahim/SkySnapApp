package com.example.skysnapproject.dataLayer.repo
import com.example.skysnapproject.dataLayer.model.CurrentWeather
import com.example.skysnapproject.dataLayer.remote.RemoteDataSource
import retrofit2.Response


class Repository private constructor(
    private val remoteDataSource: RemoteDataSource
) : RepositoryInterface {
    override suspend fun getCurrentWeather(city: String): Response<CurrentWeather> {
        TODO("Not yet implemented")
    }
}