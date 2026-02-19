package com.vecksoft.bingosorteador.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.vecksoft.bingosorteador.domain.Ball
import com.vecksoft.bingosorteador.domain.BingoEngine
import com.vecksoft.bingosorteador.domain.GameMode

class BingoViewModel : ViewModel() {

    private var engine: BingoEngine = BingoEngine(GameMode.Full)

    var currentBall by mutableStateOf<Ball?>(null)
        private set

    var previousBall by mutableStateOf<Ball?>(null)
        private set

    var drawnBalls by mutableStateOf<List<Ball>>(emptyList())
        private set

    var remainingPercentage by mutableStateOf(100)
        private set

    var isMuted by mutableStateOf(false)
        private set

    var currentMode by mutableStateOf<GameMode>(GameMode.Full)
        private set

    fun setMode(mode: GameMode) {
        currentMode = mode
        engine = BingoEngine(mode)
        reset()
    }

    fun drawNextBall() {
        val ball = engine.drawNextBall() ?: return

        previousBall = currentBall
        currentBall = ball

        drawnBalls = engine.drawnBalls()
        remainingPercentage = engine.remainingPercentage()
    }

    fun toggleMute() {
        isMuted = !isMuted
    }

    fun reset() {
        engine.reset()
        currentBall = null
        previousBall = null
        drawnBalls = emptyList()
        remainingPercentage = 100
    }
}