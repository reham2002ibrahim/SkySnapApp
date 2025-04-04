package com.example.skysnapproject.screens

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import com.example.skysnapproject.R
import com.example.skysnapproject.utils.clearSharedPrefForHome
import com.example.skysnapproject.utils.getPreference
import com.example.skysnapproject.utils.savePreference
import java.util.Locale

@Composable
fun SettingsScreen() {
    val alpha = animateAlpha()

    GradientBackground()
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
                text = stringResource(id = R.string.set_header), fontSize = 24.sp,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                style = TextStyle(brush = GradientText()),
                modifier = Modifier.alpha(alpha.value),
            )
        }

        item {
            LanguageCard()
        }
        item {
            LocationCard()
        }
        item{
            WindSpeedUnitCard()
        }
        item {
            UnitsCard()
        }
    }
}

@Composable
fun LanguageCard() {
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf(getPreference(context, "language", "English")) }
    val options = listOf(
        stringResource(id = R.string.language_english),
        stringResource(id = R.string.language_arabic)
    )

    LaunchedEffect(selectedOption) {
        savePreference(context, "language", selectedOption)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.language_label),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                options.forEach { option ->
                    Row(
                        modifier = Modifier.selectable(
                            selected = (selectedOption == option),
                            onClick = { selectedOption = option }
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedOption == option),
                            onClick = { selectedOption = option },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun LocationCard() {
    val context = LocalContext.current
    val storedPreference = getPreference(context, "location", "GPS")
    val defaultOption = stringResource(id = R.string.location_gps)
    val mapOption = stringResource(id = R.string.location_map)
    val selectedOption = remember { mutableStateOf(storedPreference ?: defaultOption) }

    val options = listOf(
        defaultOption,
        mapOption
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.location_label),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .selectable(
                                selected = (selectedOption.value == option),
                                onClick = {
                                    selectedOption.value = option
                                    savePreference(context, "location", option)
                                    if (option == mapOption) {
                                        clearSharedPrefForHome(context)
                                    }
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedOption.value == option),
                            onClick = {
                                selectedOption.value = option
                                savePreference(context, "location", option)
                                if (option == mapOption) {
                                    clearSharedPrefForHome(context)
                                }
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
/*@Composable
fun LocationCard() {
    val context = LocalContext.current
    val storedPreference = getPreference(context, "location", null.toString())
    val defaultOption = stringResource(id = R.string.location_gps)
    var selectedOption by remember { mutableStateOf(storedPreference ?: defaultOption) }


    val options = listOf(
        stringResource(id = R.string.location_gps),
        stringResource(id = R.string.location_map)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.location_label),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .selectable(
                                selected = (selectedOption == option),
                                onClick = {
                                    selectedOption = option
                                    savePreference(context, "location", option)
                                    if (option == "Map") {
                                        clearSharedPrefForHome(context)
                                    }
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedOption == option),
                            onClick = {
                                selectedOption = option
                                savePreference(context, "location", option)
                                if (option == "Map") {
                                    clearSharedPrefForHome(context)
                                }
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}*/

@Composable
fun WindSpeedUnitCard() {
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf(getPreference(context, "wind_speed_unit", "meter/sec")) }

    val options = listOf(
        Pair("meter/sec", stringResource(id = R.string.wind_s)),
        Pair("mile/hour", stringResource(id = R.string.wind_h))
    )

    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp).border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.wind_label),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                options.forEach { (unitKey, unitLabel) ->
                    Row(
                        modifier = Modifier.selectable(
                            selected = (selectedOption == unitKey),
                            onClick = {
                                selectedOption = unitKey
                                savePreference(context, "wind_speed_unit", unitKey)
                            }
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedOption == unitKey),
                            onClick = {
                                selectedOption = unitKey
                                savePreference(context, "wind_speed_unit", unitKey)
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = unitLabel, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}


@Composable
fun UnitsCard() {
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf(getPreference(context, "units", "Celsius")) }

    val options = listOf(
        Pair("Celsius", stringResource(id = R.string.units_c)),
        Pair("Kelvin", stringResource(id = R.string.units_k)),
        Pair("Fahrenheit", stringResource(id = R.string.units_F))
    )

    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp).border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.units_label),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                options.forEach { (unitKey, unitLabel) ->
                    Row(
                        modifier = Modifier.selectable(
                            selected = (selectedOption == unitKey),
                            onClick = {
                                selectedOption = unitKey
                                savePreference(context, "units", unitKey)
                            }
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedOption == unitKey),
                            onClick = {
                                selectedOption = unitKey
                                savePreference(context, "units", unitKey)
                            },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.White)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = unitLabel, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}



