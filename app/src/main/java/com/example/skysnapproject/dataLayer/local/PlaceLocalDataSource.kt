package com.example.skysnapproject.dataLayer.local

import com.example.skysnapproject.dataLayer.PlaceModels.Place
import kotlinx.coroutines.flow.Flow

class PlaceLocalDataSource(private val dao: DAO) :LocalDataSource {


    override suspend fun getFavPlaces(): Flow<List<Place>> = dao.getFavPlaces()

    override suspend fun insert(place: Place): Long = dao.insert(place)

    override suspend fun update(place: Place) {
        dao.update(place)
    }

    override suspend fun delete(place: Place): Int = dao.delete(place)
}
