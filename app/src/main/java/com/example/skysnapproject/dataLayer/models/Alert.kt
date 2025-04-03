package com.example.skysnapproject.dataLayer.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "alert")
data class Alert(
    @PrimaryKey
    var id: String,
    val fromDateTime: LocalDateTime,
    val toDateTime: LocalDateTime,
    val latitude: Double,
    val longitude: Double,
    val cityName: String,
    val type: String
)
