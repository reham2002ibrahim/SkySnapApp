package com.example.skysnapproject.utils

import android.app.Activity
import android.content.Context
import java.util.Locale

fun setAppLanguage(context: Context, language: String) {
    val locale = when (language) {
        "Arabic" -> Locale("ar", "EG")
        else -> Locale("en", "US")
    }

    Locale.setDefault(locale)
    val config = context.resources.configuration
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    if (context is Activity) {
        context.recreate()
    }
}


