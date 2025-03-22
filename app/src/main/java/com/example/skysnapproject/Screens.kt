package com.example.skysnapproject

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.airbnb.lottie.compose.*
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    GradientBackground()

    val alpha = remember {
        Animatable(0f)
    }


    LaunchedEffect(key1 = true) {
        alpha.animateTo(
            1f,
            animationSpec = tween(2400)
        )
        delay(200)
        navController.popBackStack()
        navController.navigate("Home")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoaderAnimation(
            modifier = Modifier.size(400.dp),
            anmi = R.raw.splashrain
        )
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Discover weather around you!",
            modifier = Modifier.alpha(alpha.value),
            fontSize = 28.sp
        )
    }
}

@Composable
fun LoaderAnimation(modifier: Modifier, anmi: Int) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(anmi))

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = modifier
    )
}

@Composable
fun GradientBackground() {
    val gradientColors = listOf(
        Color(0xFF6DD5FA),
        Color(0xFF9C27B0)
    )
    val gradientBrush = Brush.verticalGradient(
        colors = gradientColors,
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {}
}


/*
//@Preview
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreen() {
    GradientBackground()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(top = 70.dp, start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Filled.LocationOn, contentDescription = "LocationIcon",
                modifier = Modifier.size(40.dp)
            )

            Text(text = "cityNmae", fontSize = 26.sp)
            Spacer(modifier = Modifier.weight(.5f))
            Text(text = "ContryName", fontSize = 16.sp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "22.8 . c", fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        GlideImage(
            model = "products.thumbnail",
            contentDescription = "productImg",
            modifier = Modifier.size(140.dp)
        )

        Text(
            text = "weatherState", fontSize = 20.sp,
            textAlign = TextAlign.Center, color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Text(
                text = "curDate", fontSize = 16.sp,
                textAlign = TextAlign.Center, color = Color.DarkGray
            )
            Spacer(modifier = Modifier.width(40.dp))

            Text(
                text = "curTime", fontSize = 16.sp,
                textAlign = TextAlign.Center, color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth().padding(start = 14.dp).padding(end = 14.dp)
                .border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        )
        {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherData("Humidity", "value")
                    WeatherData("Wind speed ", "value")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherData("Pressure", "value")
                    WeatherData("Clouds", "value")
                }
            }


        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Hourly Details", fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(5) { index ->
                FancyItemCard(
                    imageRes = R.drawable.simg, time = "Time $index", temp = "tmp $index"
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Next 5 Days", fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            contentPadding = PaddingValues(vertical = 10.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(5) { index ->
                FancyRowItemCard(
                    imageRes = R.drawable.simg,
                    time = "Time $index",
                    temp = "tmp $index"
                )
            }
        }


    }

}
*/
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreen() {
    GradientBackground()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 70.dp, start = 10.dp, end = 10.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "LocationIcon",
                    modifier = Modifier.size(40.dp)
                )

                Text(text = "cityNmae", fontSize = 26.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "ContryName", fontSize = 16.sp, color = Color.Gray)
            }
        }

        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "22.8°C",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                GlideImage(
                    model = "products.thumbnail",
                    contentDescription = "productImg",
                    modifier = Modifier.size(140.dp)
                )
                Text(
                    text = "weatherState",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray
                )
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.Center) {
                Text(text = "curDate", fontSize = 16.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.width(40.dp))
                Text(text = "curTime", fontSize = 16.sp, color = Color.Gray)
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp).border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        WeatherData("Humidity", "value")
                        WeatherData("Windpeed", "value")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        WeatherData("Pressure", "value")
                        WeatherData("Cloud", "value")
                    }
                }
            }
        }

        item {
            Text(text = "Hourly Details", fontSize = 24.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
            )
        }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(5) { index ->
                    FancyItemCard(
                        imageRes = R.drawable.simg,
                        time = "Time $index",
                        temp = "tmp $index"
                    )
                }
            }
        }

        item {
            Text(
                text = "Next 5 Days", fontSize = 24.sp,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
            )
        }

        items(5) { index ->
            FancyRowItemCard(
                imageRes = R.drawable.simg,
                time = "Time $index",
                temp = "tmp $index"
            )
        }
    }
}

@Composable
fun WeatherData(key: String, value: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.LightGray)
    }

}


@Composable
fun FancyItemCard(imageRes: Int, time: String, temp: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp).border(2.dp, Color.White, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = time, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = imageRes), contentDescription = "ItemImage",
                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(20.dp)), contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = temp, fontSize = 14.sp, color = Color.LightGray
            )
        }
    }
}
@Composable
fun FancyRowItemCard(imageRes: Int, time: String, temp: String) {
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
            Image(
                painter = painterResource(id = imageRes), contentDescription = "ItemImage",
                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(20.dp)), contentScale = ContentScale.Crop
            )

            Text(
                text = time, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(start = 8.dp)
            )

            Text(text = temp, fontSize = 14.sp, color = Color.LightGray,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

    }
}

