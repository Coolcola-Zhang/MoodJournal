package com.example.moodcalendar.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.moodcalendar.ui.screen.CalendarScreen
import com.example.moodcalendar.ui.screen.DiaryListScreen
import com.example.moodcalendar.ui.screen.MoodEntryScreen
import com.example.moodcalendar.ui.screen.SettingsScreen
import com.example.moodcalendar.ui.screen.StatisticsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Calendar.route
    ) {
        composable(Screen.Calendar.route) {
            CalendarScreen(navController)
        }
        composable(Screen.MoodEntry.route) {
            MoodEntryScreen(navController)
        }
        composable(Screen.Statistics.route) {
            StatisticsScreen(navController)
        }
        composable(Screen.DiaryList.route) {
            DiaryListScreen(navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
    }
}