package com.vecksoft.bingosorteador.domain

sealed class GameMode {

    data object Full : GameMode()

    data class SingleLetter(val letter: Char) : GameMode()

    data object LetterX : GameMode()
}
