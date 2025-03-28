package com.example.skysnapproject.dataLayer.forecastModel

data class Forecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Item0>,
    val message: Int
)