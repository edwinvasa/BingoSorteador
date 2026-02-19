package com.vecksoft.bingosorteador.ui.state

import android.app.Application
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import com.vecksoft.bingosorteador.domain.Ball
import com.vecksoft.bingosorteador.domain.BallColor
import com.vecksoft.bingosorteador.domain.BingoEngine
import com.vecksoft.bingosorteador.domain.GameMode
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import android.speech.tts.TextToSpeech
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.vecksoft.bingosorteador.domain.LanguageManager
import kotlinx.coroutines.Job
import java.util.Locale

class BingoViewModel(application: Application) : AndroidViewModel(application) {

    private var engine: BingoEngine = BingoEngine(GameMode.Full())

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

    var currentMode by mutableStateOf<GameMode>(GameMode.Full())
        private set

    var isAnimating by mutableStateOf(false)
        private set

    var previewBall by mutableStateOf<Ball?>(null)
        private set

    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 80)

    var isAutoMode by mutableStateOf(false)
        private set

    private var autoJob: Job? = null

    var isRepeating by mutableStateOf(false)
        private set

    private var repeatJob: Job? = null

    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(application) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val initialLocale = LanguageManager.currentLanguage.value.toLocale()
                val result = tts?.setLanguage(initialLocale)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported: $initialLocale")
                }
            } else {
                Log.e("TTS", "Initialization failed")
            }
        }
    }

    private fun playClick() {
        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 50)
    }

    private fun getRandomAvailableBall(): Ball? {
        return engine.availableBalls().takeIf { it.isNotEmpty() }?.randomOrNull()
    }

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
        stopAutoMode()
        stopRepeating()
    }

    fun startDrawAnimation() {
        if (isAnimating) return

        isAnimating = true

        viewModelScope.launch {

            val animationDurationMs = currentMode.animationDuration
            val steps = (animationDurationMs / 100).toInt().coerceIn(10, 100)

            val startDelay = 30L
            val endDelay = 150L

            for (i in 0 until steps) {
                if (!isAnimating) break

                previewBall = getRandomAvailableBall()
                if (!isMuted) playClick()

                val progress = i.toFloat() / steps.toFloat()
                val eased = progress * progress
                val currentDelay = startDelay + ((endDelay - startDelay) * eased)

                val randomVariation = (0..20).random()

                delay(currentDelay.toLong() + randomVariation)
            }

            delay(350)
            if (!isMuted) toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP2, 150)
            finalizeDraw()
        }
    }

    fun setAnimationDuration(duration: Long) {
        if (isAnimating) return
        currentMode = when (val mode = currentMode) {
            is GameMode.Full -> mode.copy(animationDuration = duration)
            is GameMode.LetterX -> mode.copy(animationDuration = duration)
            is GameMode.CustomLetters -> mode.copy(animationDuration = duration)
        }
    }

    fun setBallColor(color: BallColor) {
        if (isAnimating) return
        currentMode = when (val mode = currentMode) {
            is GameMode.Full -> mode.copy(ballColor = color)
            is GameMode.LetterX -> mode.copy(ballColor = color)
            is GameMode.CustomLetters -> mode.copy(ballColor = color)
        }
    }

    private fun finalizeDraw() {
        previewBall = null
        drawNextBall()
        isAnimating = false

        currentBall?.let {
            if (!isMuted && !isRepeating) {
                speakBall(it)
            }
        }
    }

    fun initTTS(context: Context) {
        if (tts != null) return

        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("es", "ES")
                tts?.setSpeechRate(0.9f)
            }
        }
    }

    private fun speakBall(ball: Ball) {
        tts?.let { textToSpeech ->
            val currentLocale = LanguageManager.currentLanguage.value.toLocale()
            textToSpeech.language = currentLocale
            val textToSpeak = "${ball.letter} ${ball.number}"
            textToSpeech.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onCleared() {
        toneGenerator.release()
        tts?.stop()
        tts?.shutdown()
        super.onCleared()
    }

    fun toggleAutoMode() {
        if (isAutoMode) {
            stopAutoMode()
        } else {
            startAutoMode()
        }
    }

    private fun startAutoMode() {
        if (engine.availableBalls().isEmpty()) return

        isAutoMode = true

        autoJob = viewModelScope.launch {

            while (isAutoMode && engine.availableBalls().isNotEmpty()) {

                startDrawAnimation()

                while (isAnimating) {
                    delay(50)
                }

                delay(1500)
            }

            isAutoMode = false
        }
    }

    private fun stopAutoMode() {
        isAutoMode = false
        autoJob?.cancel()
        autoJob = null
    }

    fun repeatDrawnBalls() {

        if (drawnBalls.isEmpty()) return

        stopAutoMode()
        repeatJob?.cancel()
        isRepeating = true

        repeatJob = viewModelScope.launch {

            val bingoOrder = listOf('B', 'I', 'N', 'G', 'O')

            val grouped = drawnBalls.groupBy { it.letter }

            for (letter in bingoOrder) {

                if (!isRepeating) break

                val balls = grouped[letter] ?: continue

                val sortedNumbers = balls
                    .map { it.number }
                    .sorted()

                tts?.speak(
                    "Letra $letter",
                    TextToSpeech.QUEUE_ADD,
                    null,
                    null
                )

                delay(1200)

                for (number in sortedNumbers) {

                    if (!isRepeating) break

                    tts?.speak(
                        number.toString(),
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null
                    )

                    delay(1300)
                }

                delay(1500)
            }

            isRepeating = false
        }
    }

    fun stopRepeating() {
        isRepeating = false
        repeatJob?.cancel()
        repeatJob = null
        tts?.stop()
    }
}