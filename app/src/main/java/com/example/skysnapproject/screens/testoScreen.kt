package com.example.skysnapproject.screens
// AlarmScreen.kt
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.tasks.await


@Composable
fun TestoScreen() {
    val context = LocalContext.current
    var location by remember { mutableStateOf<Location?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            fetchLocation(context, coroutineScope,
                onLoading = { isLoading = it },
                onSuccess = { loc -> location = loc },
                onError = { err -> errorMessage = err }
            )
        } else {
            errorMessage = " access denied "
        }
    }

    LaunchedEffect(Unit) {
        fetchLocation(context, coroutineScope,
            onPermissionRequired = { permission ->
                locationPermissionLauncher.launch(permission)
            },
            onLoading = { isLoading = it },
            onSuccess = { loc -> location = loc },
            onError = { err -> errorMessage = err }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            isLoading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("  loadng ...")
                }
            }
            errorMessage != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = errorMessage!!,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        fetchLocation(context, coroutineScope,
                            onPermissionRequired = { permission ->
                                locationPermissionLauncher.launch(permission)
                            },
                            onLoading = { isLoading = it },
                            onSuccess = { loc -> location = loc },
                            onError = { err -> errorMessage = err }
                        )
                    }) {
                        Text("  try again")
                    }
                }
            }
            location != null -> {
                LocationDetails(location = location!!)
                Spacer(modifier = Modifier.height(24.dp))
                IconButton(
                    onClick = {
                        fetchLocation(context, coroutineScope,
                            onPermissionRequired = { permission ->
                                locationPermissionLauncher.launch(permission)
                            },
                            onLoading = { isLoading = it },
                            onSuccess = { loc -> location = loc },
                            onError = { err -> errorMessage = err }
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = " re fetch"
                    )
                }
            }
            else -> {
                Text("can't found location ")
            }
        }
    }
}

@Composable
private fun LocationDetails(location: Location) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = " location : ",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        LocationInfoItem(title = "lng: ", value = location.longitude.toString())
        LocationInfoItem(title = "lat:  ", value = location.latitude.toString())
        LocationInfoItem(title = "acc", value = "${location.accuracy} m")
    }
}

@Composable
private fun LocationInfoItem(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

private fun fetchLocation(
    context: Context,
    coroutineScope: CoroutineScope,
    onPermissionRequired: (String) -> Unit = { _ -> },
    onLoading: (Boolean) -> Unit,
    onSuccess: (Location) -> Unit,
    onError: (String) -> Unit
) {
    onLoading(true)
    // errorMessage = null

    coroutineScope.launch {
        try {
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                    val cancellationTokenSource = CancellationTokenSource()

                    val location = fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        cancellationTokenSource.token
                    ).await()

                    if (location != null) {
                        onSuccess(location)
                    } else {
                        onError("can't get location")
                    }
                }
                else -> {
                    onPermissionRequired(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        } catch (e: Exception) {
            onError(" error: ${e.localizedMessage ?: " unknown"}")
        } finally {
            onLoading(false)
        }
    }
}