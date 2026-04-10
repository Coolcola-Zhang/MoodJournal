package com.example.moodcalendar.data.model

import androidx.room.TypeConverter

enum class MoodType(val displayName: String, val value: Int, val emoji: String, val colorHex: String) {
    VERY_SAD("很难过", 1, "😢", "#FF6B6B"),
    SAD("难过", 2, "☹", "#FFA726"),
    NEUTRAL("平静", 3, "◐", "#42A5F5"),
    GOOD("开心", 4, "☺", "#66BB6A"),
    HAPPY("很开心", 5, "✨", "#FFD54F");

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromValue(value: Int): MoodType {
            return values().first { it.value == value }
        }

        @TypeConverter
        @JvmStatic
        fun toValue(mood: MoodType): Int {
            return mood.value
        }
    }
}