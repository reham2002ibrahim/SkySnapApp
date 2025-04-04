package com.example.skysnapproject

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.skysnapproject.navigation.BottomBar
import com.example.skysnapproject.navigation.BottomNavGraph
import com.example.skysnapproject.utils.createNotificationChannel
import com.example.skysnapproject.utils.getPreference

import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = applicationContext
        val savedLanguage = getPreference(context, "language", "English")
        val locale = when (savedLanguage) {
            "Arabic" -> Locale("ar", "EG")
            else -> Locale("en", "US")
        }

        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        createNotificationChannel(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppScreen()

        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar =( currentDestination?.route != "Splash"
            && currentDestination?.route != "map" )&& currentDestination?.route != "alertMap"

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController)
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        BottomNavGraph(navController)
    }
}
