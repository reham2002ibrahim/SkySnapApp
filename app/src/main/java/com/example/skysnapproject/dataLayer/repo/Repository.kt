package com.example.skysnapproject.dataLayer.repo

import android.location.Location
import com.example.skysnapproject.dataLayer.PlaceModels.Nominatim
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import com.example.skysnapproject.dataLayer.local.LocalDataSource
import com.example.skysnapproject.dataLayer.PlaceModels.Place
import com.example.skysnapproject.dataLayer.remote.RemoteDataSource
import com.example.skysnapproject.dataLayer.remote.RetrofitHelper.apiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource, ) : RepositoryInterface {


    override suspend fun getCurrentWeather(location: Location): Flow<CurrentWeather> {
        return remoteDataSource.getCurrentWeather(location)
    }
    override suspend fun getForecast(location: Location): Flow<Forecast> {
        return remoteDataSource.getForecast(location)
    }

    override suspend fun searchLocation(query: String): Flow<List<Nominatim>> {
        return remoteDataSource.searchLocation(query)
    }

    override suspend fun getFavPlace(): Flow<List<Place>> {
        return localDataSource.getFavPlaces()

    }


    override suspend fun addPlace(place: Place): Long {
        return localDataSource.insert(place)
    }

    override suspend fun removePlace(place: Place):  Int {
        return localDataSource.delete(place)
    }

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource,
            localDataSource: LocalDataSource
        ): Repository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repository(remoteDataSource, localDataSource).also { INSTANCE = it }
            }
        }
    }
}
