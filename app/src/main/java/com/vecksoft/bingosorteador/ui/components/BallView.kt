package com.vecksoft.bingosorteador.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vecksoft.bingosorteador.domain.BallColor

@Composable
fun BallView(
    letter: Char,
    number: Int,
    color: BallColor,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 40.sp
) {
    Box(
        modifier = modifier
            .size(300.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = color.drawableId),
            contentDescription = "Bola de bingo",
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = "$letter $number",
            color = Color.DarkGray,
            fontWeight = FontWeight.Light,
            fontSize = fontSize
        )
    }
}
