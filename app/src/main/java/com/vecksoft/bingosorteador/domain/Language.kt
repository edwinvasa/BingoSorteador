package com.vecksoft.bingosorteador.domain

import androidx.compose.runtime.mutableStateOf
import java.util.Locale

enum class Language(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    SPANISH("es", "Español"),
    FRENCH("fr", "Français"),
    ITALIAN("it", "Italiano"),
    PORTUGUESE_BR("pt-BR", "Português (BR)");

    fun toLocale(): Locale {
        return if (code.contains("-")) {
            val parts = code.split("-")
            Locale(parts[0], parts[1])
        } else {
            Locale(code)
        }
    }

    companion object {
        fun fromCode(code: String?): Language {
            val cleanCode = code?.replace("_", "-")
            return entries.find { it.code.equals(cleanCode, ignoreCase = true) } ?: ENGLISH
        }
    }
}

object LanguageManager {
    val currentLanguage = mutableStateOf(Language.fromCode(Locale.getDefault().language))
}
