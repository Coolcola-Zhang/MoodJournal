package com.example.moodcalendar.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodcalendar.data.model.DiaryEntry
import com.example.moodcalendar.data.repository.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: DiaryRepository
) : ViewModel() {
    
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()
    
    private val _entries = MutableStateFlow<List<DiaryEntry>>(emptyList())
    val entries: StateFlow<List<DiaryEntry>> = _entries.asStateFlow()
    
    init {
        loadAllEntries()
    }
    
    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }
    
    fun loadAllEntries() {
        viewModelScope.launch {
            repository.getAll().collect { entries ->
                _entries.value = entries
            }
        }
    }
    
    fun getEntryForDate(date: LocalDate): DiaryEntry? {
        return _entries.value.find { it.date == date }
    }
}