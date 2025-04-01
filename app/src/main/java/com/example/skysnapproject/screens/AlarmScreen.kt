
    package com.example.skysnapproject.screens

    import android.os.Build
    import androidx.annotation.RequiresApi
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.AccessTime
    import androidx.compose.material.icons.filled.Add
    import androidx.compose.material.icons.filled.DateRange
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.FloatingActionButton
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.OutlinedTextField
    import androidx.compose.material3.RadioButton
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
    import androidx.compose.ui.text.TextStyle
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.style.TextAlign
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.example.skysnapproject.R
    import java.time.LocalDate
    import java.time.LocalTime
    import java.time.format.DateTimeFormatter

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
    fun DateInputField(label: String, date: LocalDate, onClick: () -> Unit, modifier: Modifier = Modifier) {
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






