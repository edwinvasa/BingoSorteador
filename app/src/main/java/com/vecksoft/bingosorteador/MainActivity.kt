package com.vecksoft.bingosorteador

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.vecksoft.bingosorteador.ui.screens.BingoScreen
import com.vecksoft.bingosorteador.ui.theme.BingoSorteadorTheme
import com.vecksoft.bingosorteador.ui.LanguageWrapper
import com.vecksoft.bingosorteador.domain.LanguageManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BingoSorteadorTheme {
                val currentLanguage = LanguageManager.currentLanguage.value

                LanguageWrapper(language = currentLanguage) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        BingoScreen()
                    }
                }
            }
        }
    }
}
