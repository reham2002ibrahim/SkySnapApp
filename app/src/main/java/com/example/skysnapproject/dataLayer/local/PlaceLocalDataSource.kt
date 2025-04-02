package com.example.skysnapproject.dataLayer.local

import com.example.skysnapproject.dataLayer.models.Alert
import com.example.skysnapproject.dataLayer.models.Place
import kotlinx.coroutines.flow.Flow

class PlaceLocalDataSource(private val dao: DAO) :LocalDataSource {

    override suspend fun getFavPlaces(): Flow<List<Place>> = dao.getFavPlaces()

    override suspend fun insert(place: Place): Long = dao.insert(place)


    override suspend fun delete(place: Place): Int= dao.delete(place)


    override suspend fun getAlerts(): Flow<List<Alert>> = dao.getAlerts()

    override suspend fun insertAlert(alert: Alert): Long = dao.insertAlert(alert)

    override suspend fun deleteAlert(alert: Alert): Int = dao.deleteAlert(alert)
}
