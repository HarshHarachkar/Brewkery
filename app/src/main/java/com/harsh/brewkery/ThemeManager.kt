package com.harsh.brewkery

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {

    private const val PREFS_NAME = "brewkery_preferences"
    private const val KEY_DARK_MODE = "dark_mode"

    fun applySavedTheme(context: Context) {

        val preferences = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val isDarkMode = preferences.getBoolean(
            KEY_DARK_MODE,
            false
        )

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun toggleTheme(context: Context) {

        val preferences = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val currentMode = preferences.getBoolean(
            KEY_DARK_MODE,
            false
        )

        val newMode = !currentMode

        preferences.edit()
            .putBoolean(KEY_DARK_MODE, newMode)
            .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (newMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun isDarkMode(context: Context): Boolean {

        return context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ).getBoolean(KEY_DARK_MODE, false)
    }
}