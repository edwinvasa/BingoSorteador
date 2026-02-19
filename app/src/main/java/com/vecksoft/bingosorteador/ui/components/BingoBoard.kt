package com.vecksoft.bingosorteador.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vecksoft.bingosorteador.domain.Ball

@Composable
fun BingoBoard(
    drawnBalls: List<Ball>
) {

    val grouped = drawnBalls.groupBy { it.letter }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        listOf('B', 'I', 'N', 'G', 'O').forEach { letter ->

            val numbers = grouped[letter]
                ?.map { it.number }
                ?.sorted()
                ?: emptyList()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .border(1.dp, MaterialTheme.colorScheme.primary)
            ) {

                Text(
                    text = letter.toString(),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(numbers) { number ->
                        Text(
                            text = number.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}
