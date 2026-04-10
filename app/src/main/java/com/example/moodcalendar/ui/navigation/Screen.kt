package com.example.moodcalendar.ui.navigation

sealed class Screen(val route: String) {
    object Calendar : Screen("calendar")
    object MoodEntry : Screen("mood_entry")
    object Statistics : Screen("statistics")
    object DiaryList : Screen("diary_list")
    object Settings : Screen("settings")
}