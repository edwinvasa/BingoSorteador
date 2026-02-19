package com.vecksoft.bingosorteador.domain

import kotlin.random.Random

class BingoEngine(
    private val mode: GameMode
) {

    private val drawnBalls = mutableListOf<Ball>()
    private val availableBalls = generateInitialBalls().toMutableList()

    private fun generateInitialBalls(): List<Ball> {
        val allBalls = (1..75).map { number ->
            Ball(letterFor(number), number)
        }

        return when (mode) {
            is GameMode.Full -> allBalls

            is GameMode.SingleLetter ->
                allBalls.filter { it.letter == mode.letter }

            is GameMode.LetterX ->
                allBalls.filter { it.letter != 'N' }
        }
    }

    private fun letterFor(number: Int): Char =
        when (number) {
            in 1..15 -> 'B'
            in 16..30 -> 'I'
            in 31..45 -> 'N'
            in 46..60 -> 'G'
            in 61..75 -> 'O'
            else -> error("Invalid bingo number")
        }

    fun drawNextBall(): Ball? {
        if (availableBalls.isEmpty()) return null

        val index = Random.nextInt(availableBalls.size)
        val selected = availableBalls[index]

        drawnBalls.add(selected)
        availableBalls.removeAt(index)

        return selected
    }

    fun drawnBalls(): List<Ball> = drawnBalls.toList()

    fun remainingPercentage(): Int {
        val total = generateInitialBalls().size
        return ((availableBalls.size.toFloat() / total.toFloat()) * 100).toInt()
    }

    fun reset() {
        drawnBalls.clear()
        availableBalls.clear()
        availableBalls.addAll(generateInitialBalls())
    }

    fun availableBalls(): List<Ball> = availableBalls.toList()
}
