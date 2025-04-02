package com.example.skysnapproject.dataLayer.local

import com.example.skysnapproject.dataLayer.models.Alert
import com.example.skysnapproject.dataLayer.models.Place
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun getFavPlaces(): Flow<List<Place>>
    suspend fun insert(Place: Place):  Long
    suspend fun delete(Place: Place): Int


    suspend fun getAlerts(): Flow<List<Alert>>
    suspend fun insertAlert(alert: Alert):  Long
    suspend fun deleteAlert(alert: Alert): Int



}
