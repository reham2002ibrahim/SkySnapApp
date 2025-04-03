package com.example.skysnapproject.utils

import android.content.Context
import android.content.SharedPreferences

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