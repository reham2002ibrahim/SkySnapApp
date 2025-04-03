package com.example.skysnapproject.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.skysnapproject.dataLayer.models.Place


fun savePlaceToSharedPreferences(context: Context, place: Place) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    place.name?.let {
        editor.putString("place_name", it)
    }

    editor.putFloat("place_lat", place.lat.toFloat())
    editor.putFloat("place_lng", place.lng.toFloat())
    editor.apply()
}

fun getPlaceFromSharedPreferences(context: Context): Place? {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    val name = sharedPreferences.getString("place_name", "Unknown Location")
    val lat = sharedPreferences.getFloat("place_lat", 0f)
    val lng = sharedPreferences.getFloat("place_lng", 0f)

    return Place(
        lat = lat.toDouble(),
        lng = lng.toDouble(),
        name = name
    )
}
