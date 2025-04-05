package com.example.skysnapproject.screens

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.skysnapproject.R
import com.example.skysnapproject.dataLayer.models.Alert
import com.example.skysnapproject.dataLayer.models.Place
import com.example.skysnapproject.locationFeatch.WeatherViewModel
import com.example.skysnapproject.utils.savePlaceToSharedPreferences
import com.example.skysnapproject.utils.savePreference
import com.example.skysnapproject.utils.setSharedPref
import com.example.skysnapproject.utils.setSharedPrefForHome
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MapOfAlert(viewModel: WeatherViewModel, navController: NavController) {

    var searchQuery by remember { mutableStateOf("") }
    val searchResults by viewModel.searchLocationState.collectAsState(initial = WeatherViewModel.Response.Loading)

    var selectedPosition by remember { mutableStateOf<LatLng?>(null) }
    var showSearchResults by remember { mutableStateOf(true) }
    var showSaveButton by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(30.0240858, 31.2476), 10f)
    }
    val context = LocalContext.current

    LaunchedEffect(searchQuery) {
        if (searchQuery.length > 2) {
            viewModel.getSearchLocation(searchQuery)
        }
    }

    LaunchedEffect(searchResults) {
        if (searchResults is WeatherViewModel.Response.Success) {
            showSearchResults = true
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(top = 20.dp)) {
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

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .align(Alignment.TopCenter)) {
            TextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    showSearchResults = true
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(id = R.string.ser_loc)) },
                singleLine = true
            )

            if (showSearchResults && searchQuery.length >= 2) {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)) {
                    when (val result = searchResults) {
                        is WeatherViewModel.Response.Success -> {
                            items(result.data) { resultItem ->
                                Text(
                                    text = resultItem.display_name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            val latLng = LatLng(
                                                resultItem.lat.toDouble(),
                                                resultItem.lon.toDouble()
                                            )
                                            selectedPosition = latLng
                                            cameraPositionState.position =
                                                CameraPosition.fromLatLngZoom(latLng, 15f)
                                            showSearchResults = false
                                        }
                                        .padding(8.dp)
                                )
                            }
                        }

                        is WeatherViewModel.Response.Failure -> {}
                        is WeatherViewModel.Response.Loading -> {}
                    }
                }
            }
        }

        if (showSaveButton && selectedPosition != null) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Lat: ${selectedPosition?.latitude}, Lng: ${selectedPosition?.longitude}",
                    color = Color.Black,
                    modifier = Modifier
                        .background(Color.White)
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                FloatingActionButton(
                    onClick = {
                        viewModel.viewModelScope.launch(Dispatchers.IO) {
                            selectedPosition?.let { latLng ->
                                val adminArea = getLocationDetails(context, latLng)
                                val place = Place(
                                    name = adminArea,
                                    lat = latLng.latitude,
                                    lng = latLng.longitude
                                )

                                withContext(Dispatchers.Main) {
                                  /*  val origin = navController.previousBackStackEntry?.savedStateHandle?.get<String>("origin")
                                    if (origin == "HomeScreen") {
                                        setSharedPrefForHome(context, place)
                                        Log.i("TAG", "MapOfAlert: Data saved forHomeScree: ${place.name}")
                                    } else */
                                        savePlaceToSharedPreferences(context, place)



                                    navController.previousBackStackEntry?.savedStateHandle?.set("MAP_RESULT", true)
                                    navController.popBackStack()
                                }
                            }
                        }


                        showSaveButton = false
                    },
                    containerColor = Color(0xFF88698A)
                ) {
                    Text(text = stringResource(id = R.string.save), color = Color.White)
                }

            }
        }
    }
}



