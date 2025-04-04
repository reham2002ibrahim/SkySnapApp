package com.example.skysnapproject.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.skysnapproject.dataLayer.models.Place

fun savePreference(context: Context, key: String, value: String) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(key, value)
    editor.apply()
}

fun getPreference(context: Context, key: String, defaultValue: String): String {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE)
    return sharedPreferences.getString(key, defaultValue) ?: defaultValue
}


fun setSharedPref(context: Context, place: Place) {
    savePreference(context, "place_name", place.name ?: "Unknown")
    savePreference(context, "place_lat", place.lat.toString())
    savePreference(context, "place_lng", place.lng.toString())
}