package com.example.skysnapproject.dataLayer.repo

import android.location.Location
import com.example.skysnapproject.dataLayer.models.Nominatim
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.forecastModel.Forecast
import com.example.skysnapproject.dataLayer.local.LocalDataSource
import com.example.skysnapproject.dataLayer.models.Alert
import com.example.skysnapproject.dataLayer.models.Place
import com.example.skysnapproject.dataLayer.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class Repository  (
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

    override suspend fun getAlerts(): Flow<List<Alert>> {
        return localDataSource.getAlerts()
    }

    override suspend fun addAlert(alert: Alert): Long {
        return localDataSource.insertAlert(alert)
    }

    override suspend fun removeAlert(alert: Alert): Int {
        return localDataSource.deleteAlert(alert)

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
