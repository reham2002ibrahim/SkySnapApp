package com.example.skysnapproject.utils


import android.content.Context
import android.content.SharedPreferences
import com.example.skysnapproject.dataLayer.models.Place

fun setSharedPrefForHome(context: Context, place: Place) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("HomePreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    place.name?.let {
        editor.putString("home_place_name", it)
    }

    editor.putFloat("home_place_lat", place.lat.toFloat())
    editor.putFloat("home_place_lng", place.lng.toFloat())
    editor.apply()
}

fun getSharedPrefForHome(context: Context): Place? {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("HomePreferences", Context.MODE_PRIVATE)

    val name = sharedPreferences.getString("home_place_name", "Unknown Location")
    val lat = sharedPreferences.getFloat("home_place_lat", 0f)
    val lng = sharedPreferences.getFloat("home_place_lng", 0f)

    return Place(
        lat = lat.toDouble(),
        lng = lng.toDouble(),
        name = name
    )
}
