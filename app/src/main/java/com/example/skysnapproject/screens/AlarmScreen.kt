package com.example.skysnapproject.screens

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.skysnapproject.R
import com.example.skysnapproject.dataLayer.models.Alert
import com.example.skysnapproject.locationFeatch.WeatherViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(navController: NavController, viewModel: WeatherViewModel) {
    val allAlerts by viewModel.allAlerts.collectAsStateWithLifecycle()


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
            if (allAlerts.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .size(400.dp)
                            .padding(top = 200.dp)
                            .clickable {
                                //navController.navigate("map")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        LoaderAnimation(
                            modifier = Modifier.fillMaxSize(),
                            anmi = R.raw.addfav
                        )
                    }
                }
            } else

                items(allAlerts) { alert ->
                    AlertRowItemCard(
                        alert = alert,
                        onDelete = {
                            viewModel.viewModelScope.launch {
                                viewModel.deleteAlert(alert)
                            }
                        },
                        context = navController.context
                    )
                }


        }


        var showDialog by remember { mutableStateOf(false) }
        val mapResult = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow<Boolean?>("MAP_RESULT", null)
            ?.collectAsState()?.value

        LaunchedEffect(mapResult) {
            if (mapResult == true) {
                showDialog = false
                showDialog = true
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<Boolean>("MAP_RESULT")
            }
        }



        if (showDialog) {
            AlarmDialog(
                onDismiss = {
                    showDialog = false
                },
                navController = navController,
                viewModel = viewModel
            )
        }

        FloatingActionButton(
            onClick = {

                showDialog = true
            },

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
@Composable
fun TimeInputField(
    label: String,
    time: LocalTime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
fun DateInputField(
    label: String,
    date: LocalDate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
        onValueChange = {},
        label = { Text(text = label) },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = onClick) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date")
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun RadioButtonWithLabel(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(text = label, fontSize = 14.sp, modifier = Modifier.padding(start = 4.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlertRowItemCard(alert: Alert, onDelete: () -> Unit, context: Context = LocalContext.current) {
    val fromDateFormatted = alert.fromDateTime.toLocalDate().toString() + " " + alert.fromDateTime.toLocalTime().toString()
    val toDateFormatted = alert.toDateTime.toLocalDate().toString() + " " + alert.toDateTime.toLocalTime().toString()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(12.dp).background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                alert.cityName?.let {
                    Text(
                        text = it, fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White, maxLines = 1,
                        overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Text(
                    text = "From: $fromDateFormatted", fontSize = 16.sp,
                    color = Color.White,
                    maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(start = 8.dp)
                )
                Text(
                    text = "To: $toDateFormatted", fontSize = 16.sp,
                    color = Color.White, maxLines = 1,
                    overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(start = 8.dp)
                )
            }

            Icon(
                imageVector = Icons.Filled.DeleteForever,
                tint = Color.Blue, contentDescription = "Delete",
                modifier = Modifier
                    .size(40.dp)
                    .clickable(onClick = onDelete)
                    .padding(start = 8.dp)
            )
        }
    }
}
