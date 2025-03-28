package com.example.skysnapproject.screens

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skysnapproject.R


@Composable
fun FavoriteScreen() {
    val alpha = animateAlpha()

    GradientBackground()
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(top = 70.dp, start = 10.dp, end = 10.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

         item {
             Text(
                 text =stringResource(id = R.string.fav_header), fontSize = 24.sp,
                 fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                 style = TextStyle(brush = GradientText()),
                 modifier = Modifier.alpha(alpha.value),
                 )
         }

        items(5) { index ->
            FavRowItemCard(
                cityName = "city $index"
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FloatingActionButton(
            onClick = { /* vm add location */ },
            modifier = Modifier.align(Alignment.BottomEnd).padding(25.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color(0xFF6A0572)
            )
        }


    }

}



@Composable
fun FavRowItemCard( cityName: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp).border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp).background(Color.Transparent).padding(horizontal = 14.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = cityName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(start = 8.dp)
            )
            Icon(
                imageVector = Icons.Filled.DeleteForever,
                tint = Blue,
                contentDescription = "FavLocation",
                modifier = Modifier.size(40.dp).clickable {
                       // VM for delete
                    }
            )
        }
    }
}