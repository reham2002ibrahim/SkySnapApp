package com.example.skysnapproject.dataLayer.local

import com.example.skysnapproject.dataLayer.PlaceModels.Place
import kotlinx.coroutines.flow.Flow

class PlaceLocalDataSource(private val dao: DAO) :LocalDataSource {
    override suspend fun getAllPlaces(): List<Place> = dao.gatAll()
    override suspend fun getFavPlaces(): Flow<List<Place>> = dao.getFav()
    override suspend fun insertAll(places: List<Place>) {
        dao.insertAll(places)
    }

    override suspend fun insert(place: Place): Long = dao.insert(place)

    override suspend fun update(place: Place) {
        dao.update(place)
    }

    override suspend fun delete(product: Place): Int = dao.delete(product)
}
