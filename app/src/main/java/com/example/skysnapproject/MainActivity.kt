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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.skysnapproject.navigation.BottomBar
import com.example.skysnapproject.navigation.BottomBarRoutes
import com.example.skysnapproject.navigation.BottomNavGraph
import com.example.skysnapproject.utils.createNotificationChannel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)

        enableEdgeToEdge()
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
