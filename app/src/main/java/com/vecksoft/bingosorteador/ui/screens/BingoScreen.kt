package com.vecksoft.bingosorteador.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.vecksoft.bingosorteador.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vecksoft.bingosorteador.ui.components.AnimatedBallView
import com.vecksoft.bingosorteador.ui.components.BallView
import com.vecksoft.bingosorteador.ui.components.BasketView
import com.vecksoft.bingosorteador.ui.components.BingoBoard
import com.vecksoft.bingosorteador.ui.components.GameModeSelector
import com.vecksoft.bingosorteador.ui.state.BingoViewModel

@Composable
fun BingoScreen(
    viewModel: BingoViewModel = viewModel()
) {

    val view = LocalView.current
    DisposableEffect(Unit) {
        view.keepScreenOn = true
        onDispose {
            view.keepScreenOn = false
        }
    }

    val currentBall = viewModel.previewBall ?: viewModel.currentBall
    val previousBall = viewModel.previousBall
    val remaining = viewModel.remainingPercentage
    val isAnimating = viewModel.isAnimating
    val isMuted = viewModel.isMuted
    val previewBall = viewModel.previewBall

    var animateResult by remember { mutableStateOf(false) }
    var showGameModeSelector by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (animateResult) 1.3f else 1f,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        finishedListener = {
            animateResult = false
        }
    )

    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.initTTS(context.applicationContext)
    }

    LaunchedEffect(viewModel.currentBall) {
        if (viewModel.currentBall != null) {
            animateResult = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.app_title),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.align(Alignment.Center)
            )
            IconButton(
                onClick = { showGameModeSelector = !showGameModeSelector },
                modifier = Modifier.align(Alignment.CenterEnd),
                enabled = !isAnimating
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.configure_game_mode)
                )
            }
        }

        if (viewModel.isRepeating) {
            Text(
                text = "Repitiendo balotas...",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AnimatedVisibility(visible = showGameModeSelector) {
                GameModeSelector(
                    currentMode = viewModel.currentMode,
                    isMuted = isMuted,
                    onModeSelected = { viewModel.setMode(it) },
                    onDurationChange = { viewModel.setAnimationDuration(it) },
                    onMuteToggle = { viewModel.toggleMute() },
                    onBallColorChange = { color -> viewModel.setBallColor(color) },
                    enabled = !isAnimating,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            BasketView(
                isAnimating = isAnimating
            )
/*
            Text(
                text = "Actual",
                style = MaterialTheme.typography.titleMedium
            )
*/
            Box(
                modifier = Modifier.size(220.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isAnimating) {
                    AnimatedBallView(
                        ball = previewBall,
                        color = viewModel.currentMode.ballColor,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    if (currentBall != null) {
                        BallView(
                            letter = currentBall.letter,
                            number = currentBall.number,
                            color = viewModel.currentMode.ballColor,
                            modifier = Modifier.scale(scale),
                            //fontSize = 48.sp
                        )
                    }
                }
            }

            if (previousBall != null) {
                /*
                Text(
                    text = "Anterior",
                    style = MaterialTheme.typography.titleMedium
                )
                */
                BallView(
                    letter = previousBall.letter,
                    number = previousBall.number,
                    color = viewModel.currentMode.ballColor,
                    modifier = Modifier.height(120.dp),
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            BingoBoard(
                drawnBalls = viewModel.drawnBalls
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val animatedProgress by animateFloatAsState(
            targetValue = remaining/100f,
            animationSpec = tween(durationMillis = 500),
            label = "ProgressAnimation"
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.remaining_balls, remaining.toInt()),
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = viewModel::startDrawAnimation,
                    enabled = !isAnimating,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(stringResource(id = R.string.draw_ball))
                }

                Button(
                    onClick = { viewModel.reset() },
                    enabled = !isAnimating,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(stringResource(id = R.string.reset_game))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = viewModel::toggleAutoMode,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        if (viewModel.isAutoMode)
                            stringResource(id = R.string.stop_auto)
                        else
                            stringResource(id = R.string.auto)
                    )
                }

                if (!isMuted) {
                    Button(
                        onClick = {
                            if (viewModel.isRepeating) {
                                viewModel.stopRepeating()
                            } else {
                                viewModel.repeatDrawnBalls()
                            }
                        },
                        enabled = viewModel.drawnBalls.isNotEmpty(),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(
                            if (viewModel.isRepeating)
                                stringResource(id = R.string.stop_repeat_drawn_balls)
                            else
                                stringResource(id = R.string.repeat_drawn_balls)
                        )
                    }
                }
            }
        }
    }
}
