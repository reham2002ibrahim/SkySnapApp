package com.example.skysnapproject.dataLayer.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.skysnapproject.dataLayer.PlaceModels.Place


@Database(entities = arrayOf(Place::class), version = 1 , exportSchema = false)
abstract class PlaceDatabase : RoomDatabase() {
    abstract fun placeDao(): DAO

    companion object {
        @Volatile


        private var instanceOfDB: PlaceDatabase? = null
        fun getInstance(context: Context): PlaceDatabase {

            return instanceOfDB ?: synchronized(this) {
                val temp: PlaceDatabase =
                    Room.databaseBuilder(context, PlaceDatabase::class.java, "placedb").build()
                instanceOfDB = temp
                temp

            }
        }

    }


}