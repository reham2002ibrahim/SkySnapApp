package com.example.skysnapproject.dataLayer.PlaceModels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "place")
data class Place(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var lat:Double = 0.0,
    var lng:Double = 0.0,
    val name: String?

): Serializable
