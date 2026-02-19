package com.vecksoft.bingosorteador.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vecksoft.bingosorteador.ui.components.BingoBoard
import com.vecksoft.bingosorteador.ui.components.GameModeSelector
import com.vecksoft.bingosorteador.ui.state.BingoViewModel

@Composable
fun BingoScreen(
    viewModel: BingoViewModel = viewModel()
) {

    val currentBall = viewModel.currentBall
    val previousBall = viewModel.previousBall
    val remaining = viewModel.remainingPercentage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "BINGO",
            style = MaterialTheme.typography.headlineLarge
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameModeSelector(
                currentMode = viewModel.currentMode,
                onModeSelected = { mode ->
                    viewModel.setMode(mode)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = "Balota actual",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = currentBall?.let { "${it.letter} ${it.number}" } ?: "--",
                style = MaterialTheme.typography.displayLarge
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Restante: $remaining%")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.drawNextBall() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sortear Balota")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.reset() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reiniciar")
            }
        }
    }
}
