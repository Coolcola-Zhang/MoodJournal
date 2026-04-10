package com.example.moodcalendar.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.moodcalendar.data.database.MoodDatabase
import com.example.moodcalendar.data.repository.DiaryRepository
import com.example.moodcalendar.data.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideMoodDatabase(@ApplicationContext context: Context): MoodDatabase {
        return MoodDatabase.getInstance(context)
    }
    
    @Provides
    @Singleton
    fun provideDiaryDao(database: MoodDatabase) = database.diaryDao()
    
    @Provides
    @Singleton
    fun provideDiaryRepository(diaryDao: com.example.moodcalendar.data.database.DiaryDao): DiaryRepository {
        return DiaryRepository(diaryDao)
    }
    
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
    
    @Provides
    @Singleton
    fun provideSettingsRepository(dataStore: DataStore<Preferences>): SettingsRepository {
        return SettingsRepository(dataStore)
    }
}