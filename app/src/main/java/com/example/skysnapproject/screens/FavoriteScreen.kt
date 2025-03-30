package com.example.skysnapproject.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.skysnapproject.R
import com.example.skysnapproject.dataLayer.PlaceModels.Place
import com.example.skysnapproject.favFeatsure.FavLocationActivity
import com.example.skysnapproject.favFeatsure.FavViewModel
import kotlinx.coroutines.launch

@Composable
fun FavoriteScreen(
    navController: NavHostController, viewModel: FavViewModel
) {

    val favPlaces by viewModel.favPlaces.collectAsStateWithLifecycle()

    GradientBackground()
    var flag = 1
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
                    text = stringResource(id = R.string.fav_header),
                    fontSize = 24.sp, fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center, style = TextStyle(brush = GradientText())
                )
            }
            if (favPlaces.isEmpty()) {
                flag = 0
                item {
                    Box(
                        modifier = Modifier.size(400.dp).padding(top = 200.dp)
                            .clickable { navController.navigate("map") },
                        contentAlignment = Alignment.Center
                    ) {
                        LoaderAnimation(
                            modifier = Modifier.fillMaxSize(),
                            anmi = R.raw.addfav
                        )
                    }
                }
            } else {

                items(favPlaces.size) { index ->
                    val place = favPlaces[index]
                    place.name?.let { cityName ->
                        FavRowItemCard(
                            place, onDelete = {
                                viewModel.viewModelScope.launch {
                                    viewModel.deletePlace(place)
                                }
                            }
                        )
                    }
                }
            }

        }
        if (flag == 1){
            FloatingActionButton(
                onClick = { navController.navigate("map") },
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
}

@Composable
fun FavRowItemCard(place: Place, onDelete: () -> Unit, context: Context = LocalContext.current) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                val intent = Intent(context, FavLocationActivity::class.java).apply {
                    putExtra("PLACE", place)
                }
                context.startActivity(intent)
            }
            .border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(Color.Transparent)
                .padding(horizontal = 14.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            place.name?.let {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }
            Icon(
                imageVector = Icons.Filled.DeleteForever,
                tint = Blue, contentDescription = "Delete",
                modifier = Modifier
                    .size(40.dp)
                    .clickable(onClick = onDelete)
            )
        }
    }
}

