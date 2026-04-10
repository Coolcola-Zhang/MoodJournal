package com.example.moodcalendar.data.database

import androidx.room.*
import com.example.moodcalendar.data.model.DiaryEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DiaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entry: DiaryEntry): Long

    @Query("SELECT * FROM diary_entries WHERE date = :date")
    suspend fun getByDate(date: LocalDate): DiaryEntry?

    @Query("SELECT * FROM diary_entries WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<DiaryEntry>>

    @Query("SELECT * FROM diary_entries ORDER BY date DESC")
    fun getAll(): Flow<List<DiaryEntry>>

    @Delete
    suspend fun delete(entry: DiaryEntry)

    @Query("SELECT * FROM diary_entries WHERE content LIKE '%' || :keyword || '%' ORDER BY date DESC")
    fun search(keyword: String): Flow<List<DiaryEntry>>

    @Query("SELECT * FROM diary_entries WHERE mood = :mood ORDER BY date DESC")
    fun getByMood(mood: Int): Flow<List<DiaryEntry>>

    @Query("SELECT * FROM diary_entries WHERE tags LIKE '%' || :tag || '%' ORDER BY date DESC")
    fun getByTag(tag: String): Flow<List<DiaryEntry>>
}