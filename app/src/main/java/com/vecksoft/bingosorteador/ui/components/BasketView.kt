package com.vecksoft.bingosorteador.ui.components

import com.vecksoft.bingosorteador.R
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun BasketView(
    isAnimating: Boolean
) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.bingo_machine)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isAnimating,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),
        contentAlignment = Alignment.Center
    ) {

        if (isAnimating) {
            LottieAnimation(
                composition = composition,
                progress = { progress }
            )
        }
    }
}

