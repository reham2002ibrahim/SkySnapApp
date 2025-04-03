package com.example.skysnapproject.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.skysnapproject.BuildConfig.API_KEY
import com.example.skysnapproject.dataLayer.currentmodel.CurrentWeather
import com.example.skysnapproject.dataLayer.local.PlaceDatabase
import com.example.skysnapproject.dataLayer.models.Alert
import com.example.skysnapproject.dataLayer.remote.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.log

class AlertWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val id = inputData.getString("id") ?: ""
        val fromDateTime = LocalDateTime.parse(inputData.getString("fromDateTime"))
        val toDateTime = LocalDateTime.parse(inputData.getString("toDateTime"))
        val latitude = inputData.getDouble("latitude", 0.0)
        val longitude = inputData.getDouble("longitude", 0.0)
        val cityName = inputData.getString("cityName") ?: ""
        val type = inputData.getString("type") ?: ""

        val alert = Alert(id, fromDateTime, toDateTime, latitude, longitude, cityName, type)
        Log.i("TAGworker", "doWork: alert = $id")
        val delay = Duration.between(LocalDateTime.now(), alert.fromDateTime).toMillis()
        if (delay > 0) {
            kotlinx.coroutines.delay(delay)
        }

        return try {
            val weather = getWeatherData(alert.latitude, alert.longitude, API_KEY)
            sendNotification(weather.weather.firstOrNull()?.description ?: "No data available")
            deleteAlertFromDatabase(applicationContext, id)
            Result.success()
        } catch (e: HttpException) {
            Result.failure()
        }
    }

    private suspend fun getWeatherData(latitude: Double, longitude: Double, apiKey: String): CurrentWeather {
        return withContext(Dispatchers.IO) {
            val response =
                RetrofitHelper.apiService.getCurrentWeather(latitude, longitude, "metric", apiKey)
            if (response.isSuccessful) {
                response.body()!!
            } else {
                throw HttpException(response)
            }
        }
    }

    private fun sendNotification(weatherDescription: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, "alert_channel")
            .setContentTitle("weather status")
            .setContentText("weather status: $weatherDescription")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .build()

        notificationManager.notify(1, notification)
    }
}

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel("alert_channel", "Weather Alerts", NotificationManager.IMPORTANCE_HIGH)
            .apply {
            description = "weather alertsNotifications"
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}


private suspend fun deleteAlertFromDatabase(context: Context, alertId: String) {
    val database = PlaceDatabase.getInstance(context)
    val alertDao = database.placeDao()
    alertDao.deleteAlertById(alertId)


}
