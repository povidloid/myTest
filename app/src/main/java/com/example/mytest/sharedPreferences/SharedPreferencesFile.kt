package com.example.mytest.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ThemeManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    var isDarkTheme by mutableStateOf(true)

    init {
        isDarkTheme = sharedPreferences.getBoolean(KEY_THEME, false)
    }

    internal fun setDarkTheme(isDark: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_THEME, isDark).apply()
        isDarkTheme = isDark
    }

    companion object {
        private const val KEY_THEME = "theme"
        private const val FILE_NAME = "savedTheme"
    }
}

class HistoryManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    internal fun setHistory(history: String) {
        sharedPreferences.edit().putString(KEY_HISTORY, history).apply()
    }

    internal fun getHistory(): String? {
        return sharedPreferences.getString(KEY_HISTORY, "")
    }

    companion object {
        private const val KEY_HISTORY = "history"
        private const val FILE_NAME = "savedHistory"
    }
}