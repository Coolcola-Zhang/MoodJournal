package com.example.moodcalendar.data.repository

import com.example.moodcalendar.data.database.DiaryDao
import com.example.moodcalendar.data.model.DiaryEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class DiaryRepository @Inject constructor(
    private val diaryDao: DiaryDao
) {
    suspend fun insertOrUpdate(entry: DiaryEntry): Long {
        return diaryDao.insertOrUpdate(entry)
    }

    suspend fun getByDate(date: LocalDate): DiaryEntry? {
        return diaryDao.getByDate(date)
    }

    fun getByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<DiaryEntry>> {
        return diaryDao.getByDateRange(startDate, endDate)
    }

    fun getAll(): Flow<List<DiaryEntry>> {
        return diaryDao.getAll()
    }

    suspend fun delete(entry: DiaryEntry) {
        diaryDao.delete(entry)
    }

    fun search(keyword: String): Flow<List<DiaryEntry>> {
        return diaryDao.search(keyword)
    }

    fun getByMood(mood: Int): Flow<List<DiaryEntry>> {
        return diaryDao.getByMood(mood)
    }

    fun getByTag(tag: String): Flow<List<DiaryEntry>> {
        return diaryDao.getByTag(tag)
    }
}