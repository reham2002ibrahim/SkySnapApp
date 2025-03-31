@file:Suppress("DEPRECATION")

package com.example.skysnapproject.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AlarmAdd
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skysnapproject.R
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AlarmScreen() {

    GradientBackground()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp, start = 10.dp, end = 10.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.alarm_header),
                    fontSize = 24.sp, fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center, style = TextStyle(brush = GradientText())
                )
            }


        }
        var showDialog by remember { mutableStateOf(false) }
        if (showDialog) {
            AlarmDialog(onDismiss = { showDialog = false })
        }

        FloatingActionButton(
            onClick = { showDialog = true },

            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 16.dp),
            containerColor = Color(0xFF6A0572)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.White
            )
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmDialog(onDismiss: () -> Unit) {
    var fromTime by remember { mutableStateOf(LocalTime.of(0, 0)) }
    var showTimePickerForFrom by remember { mutableStateOf(false) }
    var toTime by remember { mutableStateOf(LocalTime.of(0, 0)) }
    var showTimePickerForTo by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth().height(350.dp),
        title = {
            Text(
                text = stringResource(id = R.string.alarmDialogH),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TimeInputField(
                        label = "From",
                        time = fromTime,
                        onClick = { showTimePickerForFrom = true },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    TimeInputField(
                        label = "To",
                        time = toTime,
                        onClick = { showTimePickerForTo = true },
                        modifier = Modifier.weight(1f)

                    )
                }

            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = "close")
            }
        }
    )

    if (showTimePickerForFrom) {
        TimePickerDialog(
            initialTime = fromTime,
            onTimeSelected = { fromTime = it },
            onDismiss = { showTimePickerForFrom = false }
        )
    }

    if (showTimePickerForTo) {
        TimePickerDialog(
            initialTime = toTime,
            onTimeSelected = { toTime = it },
            onDismiss = { showTimePickerForTo = false }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeInputField(label: String, time: LocalTime, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = time.format(DateTimeFormatter.ofPattern("HH:mm")),
        onValueChange = {},
        label = { Text(text = label) },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = onClick) {
                Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Select Time")
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimePickerDialog(
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, initialTime.hour)
        set(Calendar.MINUTE, initialTime.minute)
    }

    DisposableEffect(Unit) {
        val picker = android.app.TimePickerDialog(
            context, { _, hour, minute ->
                onTimeSelected(LocalTime.of(hour, minute))
                onDismiss()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        picker.show()
        onDispose { picker.dismiss() }
    }
}

