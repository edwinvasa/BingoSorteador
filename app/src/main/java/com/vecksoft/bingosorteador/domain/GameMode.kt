package com.vecksoft.bingosorteador.domain

sealed interface GameMode {
    val animationDuration: Long
    val ballColor: BallColor

    data class Full(
        override val animationDuration: Long = DEFAULT_ANIMATION_DURATION,
        override val ballColor: BallColor = BallColor.default
    ) : GameMode

    data class LetterX(
        override val animationDuration: Long = DEFAULT_ANIMATION_DURATION,
        override val ballColor: BallColor = BallColor.default
    ) : GameMode

    data class CustomLetters(
        val includedLetters: Set<Char>,
        override val animationDuration: Long = DEFAULT_ANIMATION_DURATION,
        override val ballColor: BallColor = BallColor.default
    ) : GameMode

    companion object {
        const val DEFAULT_ANIMATION_DURATION = 3000L
    }
}
