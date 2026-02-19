package com.vecksoft.bingosorteador.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vecksoft.bingosorteador.domain.Ball
import com.vecksoft.bingosorteador.domain.BallColor

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedBallView(
    ball: Ball?,
    color: BallColor,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = ball,
        modifier = modifier,
        transitionSpec = {
            val slideIn = slideInHorizontally(
                animationSpec = tween(durationMillis = 150),
                initialOffsetX = { fullWidth -> -fullWidth }
            )

            val slideOut = slideOutHorizontally(
                animationSpec = tween(durationMillis = 150),
                targetOffsetX = { fullWidth -> fullWidth }
            )

            (slideIn with slideOut).using(
                SizeTransform(clip = false)
            )
        },
        label = "AnimatedBall"
    ) { targetBall ->
        if (targetBall != null) {
            BallView(letter = targetBall.letter, number = targetBall.number, color = color)
        } else {
            BallView(letter = ' ', number = 0, color = color)
        }
    }
}
