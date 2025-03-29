package com.example.skysnapproject.screens

import android.content.Context
import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.skysnapproject.dataLayer.PlaceModels.Place
import com.example.skysnapproject.locationFeatch.WeatherViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch


@Composable
fun MapScreen(viewModel: WeatherViewModel) {

    var selectedPosition by remember { mutableStateOf<LatLng?>(null) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(30.0240858, 31.2476), 10f)
    }

    var showSaveButton by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize().padding(top = 50.dp)) {
        Column {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(5.dp),
                placeholder = { Text("Search for a location") }, singleLine = true,
                trailingIcon = {
                    IconButton(onClick = {
                        val geocoder = Geocoder(context)
                        try {
                            val addresses = geocoder.getFromLocationName(searchQuery, 1)
                            if (addresses != null && addresses.isNotEmpty()) {
                                val location = addresses[0]
                                val latLng = LatLng(location.latitude, location.longitude)
                                selectedPosition = latLng
                                cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 10f)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(zoomControlsEnabled = true),
                onMapClick = { latLng ->
                    selectedPosition = latLng
                    showSaveButton = true
                }
            ) {
                selectedPosition?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "Selected Location",
                        snippet = "Lat: ${it.latitude}, Lng: ${it.longitude}"
                    )
                }
            }
        }

        if (showSaveButton && selectedPosition != null) {
            Column(
                modifier = Modifier.align(Alignment.Center).padding(16.dp)
            ) {
                Text(
                    text = "Lat: ${selectedPosition?.latitude}, Lng: ${selectedPosition?.longitude}",
                    color = Color.Black,
                    modifier = Modifier.background(Color.White).padding(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                FloatingActionButton(
                    onClick = {
                        selectedPosition?.let { latLng ->
                            val adminArea = getAdminAreaName(context, latLng)

                            val place = Place(
                                name = adminArea
                            )
                            viewModel.viewModelScope.launch {
                                viewModel.saveLocation(place)
                            }
                        }
                        showSaveButton = false
                    },
                    containerColor = Color(0xFF88698A)
                ) {
                    Text(text = "Save", color = Color.White)
                }
            }
        }
    }
}
private fun getAdminAreaName(context: Context, latLng: LatLng): String {
    return try {
        val geocoder = Geocoder(context)
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        addresses?.firstOrNull()?.adminArea ?: "Unknown"
    } catch (e: Exception) {
        "Unknown"
    }
}
