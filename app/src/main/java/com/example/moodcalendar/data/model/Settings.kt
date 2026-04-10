package com.example.moodcalendar.data.model

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object SettingsKeys {
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
    val REMINDER_HOUR = intPreferencesKey("reminder_hour")
    val REMINDER_MINUTE = intPreferencesKey("reminder_minute")
}

data class AppSettings(
    val themeMode: String = "system", // system, light, dark
    val reminderEnabled: Boolean = false,
    val reminderHour: Int = 20, // 20:00
    val reminderMinute: Int = 0
)