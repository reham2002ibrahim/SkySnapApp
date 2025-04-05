package com.example.skysnapproject.dataLayer.repo

import com.example.skysnapproject.dataLayer.local.LocalDataSource
import com.example.skysnapproject.dataLayer.models.Alert
import com.example.skysnapproject.dataLayer.models.Place
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeLocalDataSource() : LocalDataSource {

    private val allPlaces = MutableStateFlow<List<Place>>(emptyList())

    override suspend fun getFavPlaces(): Flow<List<Place>> {
        return allPlaces
    }

    override suspend fun insert(Place: Place): Long {

        allPlaces.update { it + Place }
        return 1L
    }

    override suspend fun delete(Place: Place): Int {
        val sz = allPlaces.value.size
        allPlaces.update { it.filterNot { it == Place } }
        return if (allPlaces.value.size < sz) 1 else 0
    }

    override suspend fun getAlerts(): Flow<List<Alert>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(alert: Alert): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alert: Alert): Int {
        TODO("Not yet implemented")
    }
}