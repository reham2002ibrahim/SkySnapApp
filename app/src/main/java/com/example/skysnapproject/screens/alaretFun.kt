package com.example.skysnapproject.screens

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.skysnapproject.R
import com.example.skysnapproject.dataLayer.models.Alert
import com.example.skysnapproject.locationFeatch.WeatherViewModel
import com.example.skysnapproject.utils.AlertWorker
import com.example.skysnapproject.utils.getPlaceFromSharedPreferences
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.time.Duration
import java.util.UUID
import kotlin.math.log

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlarmDialog(onDismiss: () -> Unit, navController: NavController, viewModel: WeatherViewModel) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
    // sharedPreferences.edit().clear().apply()
    var fromTime by remember { mutableStateOf(LocalTime.now()) }
    var showTimePickerForFrom by remember { mutableStateOf(false) }
    var toTime by remember { mutableStateOf(LocalTime.now().plusHours(1)) }
    var showTimePickerForTo by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Alarm") }

    var fromDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePickerForFrom by remember { mutableStateOf(false) }
    var toDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePickerForTo by remember { mutableStateOf(false) }

    val currentTime = LocalTime.now()
    val currentDate = LocalDate.now()


    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    val place = getPlaceFromSharedPreferences(context)

    var isLocationSelected by remember { mutableStateOf(place?.lat != 0.0 && place?.lng != 0.0) }


    fun validateInputs(): Boolean {
        var flag = true
        if (place != null && place.lat == 0.0 && place.lng == 0.0) {
            showToast("No place data found, please select a location first")
            flag = false
            isLocationSelected = false
        }
        if (!flag) return false
        return when {
            fromDate == currentDate && fromTime.isBefore(currentTime) -> {
                showToast("مينفعش تبدا زمان")
                false
            }

            toDate.isBefore(fromDate) -> {
                showToast("مينفعش البدايه تبقا بعد النهايه ")
                false
            }

            toDate == fromDate && toTime.isBefore(fromTime) -> {
                showToast("نهايه الوقت لازم اكبر من دابتو ")
                false
            }

            else -> true
        }
    }


    AlertDialog(
        onDismissRequest = {
            onDismiss()
            isLocationSelected = false

        },
        modifier = Modifier
            .fillMaxWidth()
            .height(650.dp),
        title = {
            Text(
                text = stringResource(id = R.string.alarmDialogH),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {


                Button(
                    onClick = {
                        navController.navigate("alertMap")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = if (isLocationSelected) stringResource(id = R.string.loc_selected) else stringResource(id = R.string.chose_loc),
                        color = Color.White
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DateInputField(
                        label = stringResource(id = R.string.fromdate),
                        date = fromDate,
                        onClick = {
                            if (isLocationSelected) showDatePickerForFrom = true
                            else showToast("Select Location First")
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TimeInputField(
                        label = stringResource(id = R.string.from_time),
                        time = fromTime,
                        onClick = {
                            if (isLocationSelected) showTimePickerForFrom = true
                            else showToast("Select Location First")
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DateInputField(
                        label = stringResource(id = R.string.todate),
                        date = toDate,
                        onClick = {
                            if (isLocationSelected) showDatePickerForTo = true
                            else showToast("Select Location First")
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TimeInputField(
                        label = stringResource(id = R.string.totime),
                        time = toTime,
                        onClick = {
                            if (isLocationSelected) showTimePickerForTo =
                                true else showToast("Select Location First")
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    RadioButtonWithLabel(
                        label = stringResource(id = R.string.notification),
                        selected = selectedOption == "Notification",
                        onClick = { selectedOption = "Notification" }
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButtonWithLabel(
                        label = stringResource(id = R.string.alarm),
                        selected = selectedOption == "Alarm",
                        onClick = { selectedOption = "Alarm" }
                    )
                }

            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        if (validateInputs()) {
                            val place = getPlaceFromSharedPreferences(context)
                            val alertId = UUID.randomUUID().toString()

                            if (place != null) {
                                val alert = Alert(
                                    id = alertId,
                                    fromDateTime = LocalDateTime.of(fromDate, fromTime),
                                    toDateTime = LocalDateTime.of(toDate, toTime)
                                        .truncatedTo(ChronoUnit.SECONDS),
                                    latitude = place.lat,
                                    longitude = place.lng,
                                    cityName = getLocationDetails(
                                        context,
                                        LatLng(place.lat, place.lng)
                                    ),
                                    type = selectedOption
                                )
                                val workRequest = OneTimeWorkRequestBuilder<AlertWorker>()
                                    .setInputData(
                                        workDataOf(
                                            "id" to alert.id,
                                            "fromDateTime" to alert.fromDateTime.toString(),
                                            "toDateTime" to alert.toDateTime.toString(),
                                            "latitude" to alert.latitude,
                                            "longitude" to alert.longitude,
                                            "cityName" to alert.cityName,
                                            "type" to alert.type
                                        )
                                    )
                                    .build()

                                val alertId = workRequest.id.toString()
                                alert.id = alertId
                                Log.i("TAG", "AlarmDialog: $alertId")
                                WorkManager.getInstance(context).enqueue(workRequest)
                                viewModel.saveAlert(alert)

                                sharedPreferences.edit().clear().apply()

                                val savedData =
                                    "From: ${fromDate} ${fromTime}, To: ${toDate} ${toTime}, Type: $selectedOption"
                                Log.d("AlarmDialog", "Saved Data: $savedData")

                                onDismiss()
                            } else {
                                showToast("No place data found, please select a location first")
                            }
                        }
                    }
                ) {
                    Text(text =stringResource(id = R.string.save))
                }

            }
        }
    )

    if (showTimePickerForFrom) {
        TimePickerDialog(
            initialTime = fromTime,
            onTimeSelected = { newTime ->
                fromTime = newTime
                showTimePickerForFrom = false
            },
            onDismiss = { showTimePickerForFrom = false }
        )
    }

    if (showTimePickerForTo) {
        TimePickerDialog(
            initialTime = toTime,
            onTimeSelected = { newTime ->
                toTime = newTime
                showTimePickerForTo = false
            },
            onDismiss = { showTimePickerForTo = false }
        )
    }

    if (showDatePickerForFrom) {
        DatePickerDialog(
            initialDate = fromDate,
            onDateSelected = { newDate ->
                fromDate = newDate
                showDatePickerForFrom = false
            },
            onDismiss = { showDatePickerForFrom = false }
        )
    }

    if (showDatePickerForTo) {
        DatePickerDialog(
            initialDate = toDate,
            onDateSelected = { newDate ->
                toDate = newDate
                showDatePickerForTo = false
            },
            onDismiss = { showDatePickerForTo = false }
        )
    }
}

