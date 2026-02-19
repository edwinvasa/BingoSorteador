package com.vecksoft.bingosorteador.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vecksoft.bingosorteador.domain.GameMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameModeSelector(
    currentMode: GameMode,
    onModeSelected: (GameMode) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    val modeLabel = when (currentMode) {
        is GameMode.Full -> "Cartón completo"
        is GameMode.SingleLetter -> "Letra ${currentMode.letter}"
        is GameMode.LetterX -> "Sin números de la letra N"
    }

    Column {

        Text("Modalidad de juego")

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            OutlinedTextField(
                value = modeLabel,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                label = { Text("Seleccionar modalidad") }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                DropdownMenuItem(
                    text = { Text("Cartón completo") },
                    onClick = {
                        onModeSelected(GameMode.Full)
                        expanded = false
                    }
                )

                listOf('B', 'I', 'N', 'G', 'O').forEach { letter ->
                    DropdownMenuItem(
                        text = { Text("Solo letra $letter") },
                        onClick = {
                            onModeSelected(GameMode.SingleLetter(letter))
                            expanded = false
                        }
                    )
                }

                DropdownMenuItem(
                    text = { Text("Sin números de la letra N") },
                    onClick = {
                        onModeSelected(GameMode.LetterX)
                        expanded = false
                    }
                )
            }
        }
    }
}
