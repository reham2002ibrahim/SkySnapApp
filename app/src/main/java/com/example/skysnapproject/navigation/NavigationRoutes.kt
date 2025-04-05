package com.example.skysnapproject.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

import com.example.skysnapproject.R
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomBarRoutes(
    val route: String,
    @StringRes val titleResId: Int,
    @DrawableRes val icon: Int

) {
    @Serializable
    object HomeRoute : BottomBarRoutes("home", R.string.nav_home, R.drawable.home)

    @Serializable
    object FavRoute : BottomBarRoutes("favorites", R.string.nav_fav, R.drawable.fav)

    @Serializable
    object AlarmRoute : BottomBarRoutes("alarm", R.string.nav_alrm, R.drawable.alarm)

    @Serializable
    object SettingsRoute : BottomBarRoutes("settings", R.string.nav_set, R.drawable.settings)


}