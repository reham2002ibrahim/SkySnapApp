package com.example.skysnapproject.dataLayer.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "alert")
data class Alert(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fromDateTime: LocalDateTime,
    val toDateTime: LocalDateTime,
    val latitude: Double,
    val longitude: Double,
    val cityName: String,
    val type: String
)
