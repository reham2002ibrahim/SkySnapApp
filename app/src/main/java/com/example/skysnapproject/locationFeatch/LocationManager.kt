package com.example.skysnapproject.locationFeatch
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await

class LocationManager(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var currentCity: String? = null
        private set

    @SuppressLint("MissingPermission")
    suspend fun fetchLocation() {
        try {
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).await()

            val finalLocation = location ?: fusedLocationClient.lastLocation.await()

            finalLocation?.let {
                currentCity = getCityName(it)
                Log.i("TAG", "fetchLocation: $currentCity")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }




   private  fun getCityName(location: Location): String {
        return try {
            val geocoder = Geocoder(context)
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            addresses?.firstOrNull()?.adminArea ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }


}
