package com.example.skysnapproject.dataLayer.local

import com.example.skysnapproject.dataLayer.PlaceModels.Place
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun getFavPlaces(): Flow<List<Place>>
    suspend fun insert(Place: Place):  Long
    suspend fun delete(Place: Place): Int
}
