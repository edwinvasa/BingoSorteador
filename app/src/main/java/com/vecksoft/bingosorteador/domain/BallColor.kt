package com.vecksoft.bingosorteador.domain

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.vecksoft.bingosorteador.R

enum class BallColor(
    @DrawableRes val drawableId: Int,
    @StringRes val nameResId: Int
) {
    RED(R.drawable.ball_red, R.string.color_red),
    BLUE(R.drawable.ball_blue, R.string.color_blue),
    GREEN(R.drawable.ball_green, R.string.color_green),
    ORANGE(R.drawable.ball_orange, R.string.color_orange),
    PURPLE(R.drawable.ball_purple, R.string.color_purple),
    WHITE(R.drawable.ball_white, R.string.color_white),
    YELLOW(R.drawable.ball_yellow, R.string.color_yellow);

    companion object {
        val default = RED
    }
}
