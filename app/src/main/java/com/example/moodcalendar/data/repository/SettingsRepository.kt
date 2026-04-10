package com.example.moodcalendar.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.moodcalendar.data.model.AppSettings
import com.example.moodcalendar.data.model.SettingsKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    
    fun getSettings(): Flow<AppSettings> {
        return dataStore.data.map { preferences ->
            AppSettings(
                themeMode = preferences[SettingsKeys.THEME_MODE] ?: "system",
                reminderEnabled = preferences[SettingsKeys.REMINDER_ENABLED] ?: false,
                reminderHour = preferences[SettingsKeys.REMINDER_HOUR] ?: 20,
                reminderMinute = preferences[SettingsKeys.REMINDER_MINUTE] ?: 0
            )
        }
    }
    
    suspend fun setThemeMode(themeMode: String) {
        dataStore.edit { preferences ->
            preferences[SettingsKeys.THEME_MODE] = themeMode
        }
    }
    
    suspend fun setReminderEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[SettingsKeys.REMINDER_ENABLED] = enabled
        }
    }
    
    suspend fun setReminderTime(hour: Int, minute: Int) {
        dataStore.edit { preferences ->
            preferences[SettingsKeys.REMINDER_HOUR] = hour
            preferences[SettingsKeys.REMINDER_MINUTE] = minute
        }
    }
}