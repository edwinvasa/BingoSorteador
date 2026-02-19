package com.vecksoft.bingosorteador.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
    //val remaining = viewModel.remainingPercentage
    val isAnimating = viewModel.isAnimating

    var animateResult by remember { mutableStateOf(false) }

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
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "BINGO",
            style = MaterialTheme.typography.headlineLarge
        )

        if (viewModel.isRepeating) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Repitiendo balotas...",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            GameModeSelector(
                currentMode = viewModel.currentMode,
                onModeSelected = { mode ->
                    viewModel.setMode(mode)
                }
            )

            BasketView(
                isAnimating = isAnimating
            )

            Text(
                text = "Balota actual",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentBall?.let { "${it.letter} ${it.number}" } ?: "--",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.scale(scale)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Balota anterior",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = previousBall?.let { "${it.letter} ${it.number}" } ?: "--",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            BingoBoard(
                drawnBalls = viewModel.drawnBalls
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

       /*
        Text("Restante: $remaining%")

        Spacer(modifier = Modifier.height(16.dp))
        */

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
                    Text("Sortear")
                }

                Button(
                    onClick = { viewModel.reset() },
                    enabled = !isAnimating,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("Reiniciar")
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
                            "Detener Auto"
                        else
                            "Auto"
                    )
                }

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
                            "Detener Rep."
                        else
                            "Repetir"
                    )
                }
            }
        }
    }
}
