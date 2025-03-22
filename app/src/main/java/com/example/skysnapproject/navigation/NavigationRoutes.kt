package com.example.skysnapproject.navigation

import com.example.skysnapproject.R
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomBarRoutes(
    val title: String,
    val icon: Int
) {
    @Serializable
    object HomeRoute : BottomBarRoutes("Home", R.drawable.home)

    @Serializable
    object FavRoute : BottomBarRoutes("Favorites", R.drawable.fav)

    @Serializable
    object AlarmRoute : BottomBarRoutes("Alarm", R.drawable.alarm)

    @Serializable
    object SettingsRoute : BottomBarRoutes("Settings", R.drawable.settings)
}