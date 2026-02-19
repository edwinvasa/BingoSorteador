package com.vecksoft.bingosorteador.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vecksoft.bingosorteador.domain.BallColor
import com.vecksoft.bingosorteador.domain.GameMode
import com.vecksoft.bingosorteador.R
import com.vecksoft.bingosorteador.domain.Language
import com.vecksoft.bingosorteador.domain.LanguageManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameModeSelector(
    currentMode: GameMode,
    isMuted: Boolean,
    onModeSelected: (GameMode) -> Unit,
    onDurationChange: (Long) -> Unit,
    onMuteToggle: () -> Unit,
    onBallColorChange: (BallColor) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {

    val bingoLetters = listOf('B', 'I', 'N', 'G', 'O')

    val selectedLetters by remember(currentMode) {
        mutableStateOf(
            (currentMode as? GameMode.CustomLetters)?.includedLetters ?: bingoLetters.toSet()
        )
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(id = R.string.game_mode_title),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { onModeSelected(GameMode.Full()) },
                colors = if (currentMode is GameMode.Full) ButtonDefaults.buttonColors() else ButtonDefaults.outlinedButtonColors(),
                border = if (currentMode is GameMode.Full) null else ButtonDefaults.outlinedButtonBorder,
                enabled = enabled
            ) {
                Text(stringResource(id = R.string.game_mode_full))
            }

            Button(
                onClick = { onModeSelected(GameMode.LetterX()) },
                colors = if (currentMode is GameMode.LetterX) ButtonDefaults.buttonColors() else ButtonDefaults.outlinedButtonColors(),
                border = if (currentMode is GameMode.LetterX) null else ButtonDefaults.outlinedButtonBorder,
                enabled = enabled
            ) {
                Text(stringResource(id = R.string.game_mode_no_n))
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.game_mode_custom),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(bingoLetters) { letter ->
                val isSelected = letter in selectedLetters
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        if (enabled) {
                            val newSelection = if (isSelected) {
                                selectedLetters - letter
                            } else {
                                selectedLetters + letter
                            }
                            if (newSelection.isNotEmpty()) {
                                onModeSelected(
                                    GameMode.CustomLetters(
                                        includedLetters = newSelection,
                                        animationDuration = currentMode.animationDuration
                                    )
                                )
                            }
                        }
                    },
                    label = { Text(letter.toString()) }
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val durationInSeconds = currentMode.animationDuration / 1000f
            Text(
                text = stringResource(id = R.string.draw_duration, durationInSeconds),
                style = MaterialTheme.typography.bodyMedium
            )

            Slider(
                value = currentMode.animationDuration.toFloat(),
                onValueChange = { newValue ->
                    onDurationChange(newValue.toLong())
                },
                valueRange = 1000f..10000f,
                steps = 8,
                enabled = enabled,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.game_sound_and_voice),
                style = MaterialTheme.typography.bodyMedium
            )
            Switch(
                checked = !isMuted,
                onCheckedChange = { _ -> onMuteToggle() },
                enabled = enabled
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.ball_color),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(BallColor.entries.toTypedArray()) { color ->
                FilterChip(
                    selected = currentMode.ballColor == color,
                    onClick = {
                        if (enabled) {
                            onBallColorChange(color)
                        }
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(id = color.drawableId),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text(
                        stringResource(id = color.nameResId))
                    }
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = "Idioma / Language",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(8.dp))

        var languageMenuExpanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = languageMenuExpanded,
            onExpandedChange = { if (enabled) languageMenuExpanded = !languageMenuExpanded },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = LanguageManager.currentLanguage.value.displayName,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = languageMenuExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = languageMenuExpanded,
                onDismissRequest = { languageMenuExpanded = false }
            ) {
                Language.entries.forEach { language ->
                    DropdownMenuItem(
                        text = { Text(language.displayName) },
                        onClick = {
                            LanguageManager.currentLanguage.value = language
                            languageMenuExpanded = false
                        }
                    )
                }
            }
        }
    }
}