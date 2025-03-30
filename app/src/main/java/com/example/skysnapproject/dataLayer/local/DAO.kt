package com.example.skysnapproject.dataLayer.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.skysnapproject.dataLayer.PlaceModels.Place
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {


    @Query("Select *from place")
    fun getFavPlaces(): Flow<List<Place>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert (place: Place) :Long



    @Delete
    suspend fun delete (place: Place) : Int


/*
/*
    @Query("Select *from place")
    suspend fun gatAll() : List<Place>
*/

    @Query("Select *from place")
    suspend fun getFav() : List<Place>
*/


/*
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(places : List<Place>)
*/

    /*    @Update
        suspend fun update (place: Place)*/

}