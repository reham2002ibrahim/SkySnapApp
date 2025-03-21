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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    val alpha = remember {
        Animatable(0f)
    }


    LaunchedEffect(key1 = true) {
        alpha.animateTo(1f,
        animationSpec= tween(2400) )
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
fun HomeScreen(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "home screen ",
            fontSize = 28.sp
        )
    }
}
