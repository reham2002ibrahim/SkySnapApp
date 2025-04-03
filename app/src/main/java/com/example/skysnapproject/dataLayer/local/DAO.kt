package com.example.skysnapproject.dataLayer.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import com.example.skysnapproject.dataLayer.models.Alert
import com.example.skysnapproject.dataLayer.models.Place
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {


    @Query("Select *from place")
    fun getFavPlaces(): Flow<List<Place>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert (place: Place) :Long



    @Delete
    suspend fun delete (place: Place) : Int



    @Query("Select *from alert")
    fun getAlerts(): Flow<List<Alert>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlert (alert: Alert) :Long



    @Delete
    suspend fun deleteAlert (alert: Alert) : Int

    @Query("DELETE FROM alert WHERE id = :alertId")
    suspend fun deleteAlertById(alertId: String)




}