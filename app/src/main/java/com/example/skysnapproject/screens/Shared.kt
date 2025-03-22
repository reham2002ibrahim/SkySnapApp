package com.example.skysnapproject.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

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
fun animateAlpha(
    initialValue: Float = 0f,
    targetValue: Float = 1f,
    durationMillis: Int = 2400): Animatable<Float, AnimationVector1D> {
    val alpha = remember {
        Animatable(initialValue)
    }
    LaunchedEffect(key1 = true) {
        alpha.animateTo(
            targetValue,
            animationSpec = tween(durationMillis)
        )
    }
    return alpha
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

@Composable
fun GradientText(): Brush {
    return Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFE91E63),
            Color(0xFF184050)
            // Color.White
        )
    )
}


