package com.example.skysnapproject.dataLayer.local

import com.example.skysnapproject.dataLayer.PlaceModels.Place
import kotlinx.coroutines.flow.Flow

class PlaceLocalDataSource(private val dao: DAO) :LocalDataSource {

    override suspend fun getFavPlaces(): Flow<List<Place>> = dao.getFavPlaces()

    override suspend fun insert(place: Place): Flow<Long> = dao.insert(place)


    override suspend fun delete(place: Place): Flow<Int> = dao.delete(place)
}
