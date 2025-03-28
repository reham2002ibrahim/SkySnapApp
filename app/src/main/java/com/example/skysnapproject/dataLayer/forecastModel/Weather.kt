package com.example.skysnapproject.dataLayer.forecastModel

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)