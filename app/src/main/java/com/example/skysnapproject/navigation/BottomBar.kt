package com.example.skysnapproject.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavHostController) {
    val listOfScreens = listOf(
        BottomBarRoutes.HomeRoute,
        BottomBarRoutes.FavRoute,
        BottomBarRoutes.AlarmRoute,
        BottomBarRoutes.SettingsRoute
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var selectedItem by remember { mutableStateOf(0) }

    NavigationBar(
        modifier = Modifier
            .height(84.dp)
            .background(color = Color.White, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(horizontal = 8.dp),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        listOfScreens.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        tint = if (selectedItem == index) Color(0xFF6200EE) else Color.Gray
                    )
                },
                label = {
                    Text(text =  stringResource(id = item.titleResId), fontSize = 18.sp, color = if (selectedItem == index) Color(0xFF6200EE) else Color.Gray)
                },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF6200EE),
                    selectedTextColor = Color(0xFF6200EE),
                    indicatorColor = Color.White
                )
            )
        }
    }
}