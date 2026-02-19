package com.vecksoft.bingosorteador.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.vecksoft.bingosorteador.domain.Language
import java.util.Locale

@Composable
@ReadOnlyComposable
private fun getUpdatedConfiguration(language: Language): Configuration {
    val currentConfiguration = LocalConfiguration.current
    val context = LocalContext.current
    val newConfiguration = Configuration(currentConfiguration)

    val locale = language.toLocale()
    Locale.setDefault(locale)

    newConfiguration.setLocale(locale)
    newConfiguration.setLayoutDirection(locale)

    @Suppress("DEPRECATION")
    context.resources.updateConfiguration(newConfiguration, context.resources.displayMetrics)

    return newConfiguration
}

@Composable
fun LanguageWrapper(
    language: Language,
    content: @Composable () -> Unit
) {
    val newConfiguration = getUpdatedConfiguration(language)

    CompositionLocalProvider(
        LocalConfiguration provides newConfiguration,
        content = content
    )
}
