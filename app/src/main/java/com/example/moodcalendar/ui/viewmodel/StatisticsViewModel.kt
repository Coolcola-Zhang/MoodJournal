package com.example.moodcalendar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodcalendar.data.model.DiaryEntry
import com.example.moodcalendar.data.model.MoodType
import com.example.moodcalendar.data.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

data class MoodStatistics(
    val moodDistribution: Map<MoodType, Int> = emptyMap(),
    val moodTrend: List<Pair<LocalDate, Int>> = emptyList(),
    val tagStatistics: Map<String, Int> = emptyMap(),
    val consecutiveDays: Int = 0
)

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: DiaryRepository
) : ViewModel() {
    
    private val _statistics = MutableStateFlow(MoodStatistics())
    val statistics: StateFlow<MoodStatistics> = _statistics.asStateFlow()
    
    private val _selectedPeriod = MutableStateFlow(YearMonth.now())
    val selectedPeriod: StateFlow<YearMonth> = _selectedPeriod.asStateFlow()
    
    init {
        loadStatistics()
    }
    
    fun setPeriod(period: YearMonth) {
        _selectedPeriod.value = period
        loadStatistics()
    }
    
    private fun loadStatistics() {
        viewModelScope.launch {
            repository.getAll().collect { entries ->
                val periodEntries = entries.filter { 
                    YearMonth.from(it.date) == _selectedPeriod.value 
                }
                
                val statistics = calculateStatistics(periodEntries)
                _statistics.value = statistics
            }
        }
    }
    
    private fun calculateStatistics(entries: List<DiaryEntry>): MoodStatistics {
        // 心情分布统计
        val moodDistribution = entries.groupBy { it.mood }
            .mapValues { it.value.size }
            .toMutableMap()
        
        // 确保所有心情类型都有数据
        MoodType.values().forEach { mood ->
            if (!moodDistribution.containsKey(mood)) {
                moodDistribution[mood] = 0
            }
        }
        
        // 心情趋势（最近30天）
        val endDate = LocalDate.now()
        val startDate = endDate.minusDays(29)
        val moodTrend = (0..29).map { offset ->
            val date = endDate.minusDays(offset.toLong())
            val entry = entries.find { it.date == date }
            val moodValue = entry?.mood?.value ?: 0
            date to moodValue
        }.reversed()
        
        // 标签统计
        val tagStatistics = entries.flatMap { it.tags }
            .groupBy { it }
            .mapValues { it.value.size }
        
        // 连续记录天数
        val consecutiveDays = calculateConsecutiveDays(entries)
        
        return MoodStatistics(
            moodDistribution = moodDistribution,
            moodTrend = moodTrend,
            tagStatistics = tagStatistics,
            consecutiveDays = consecutiveDays
        )
    }
    
    private fun calculateConsecutiveDays(entries: List<DiaryEntry>): Int {
        val sortedDates = entries.map { it.date }.sortedDescending()
        if (sortedDates.isEmpty()) return 0
        
        var consecutive = 1
        for (i in 1 until sortedDates.size) {
            if (sortedDates[i - 1].minusDays(1) == sortedDates[i]) {
                consecutive++
            } else {
                break
            }
        }
        
        return consecutive
    }
}