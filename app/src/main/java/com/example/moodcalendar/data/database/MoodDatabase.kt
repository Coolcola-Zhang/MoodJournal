package com.example.moodcalendar.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moodcalendar.data.model.DiaryEntry

@Database(
    entities = [DiaryEntry::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class, com.example.moodcalendar.data.model.MoodType::class)
abstract class MoodDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao

    companion object {
        @Volatile
        private var INSTANCE: MoodDatabase? = null

        fun getInstance(context: Context): MoodDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MoodDatabase::class.java,
                    "mood_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}