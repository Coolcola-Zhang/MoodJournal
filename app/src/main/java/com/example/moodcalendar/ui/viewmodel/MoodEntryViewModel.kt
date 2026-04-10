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
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MoodEntryViewModel @Inject constructor(
    private val repository: DiaryRepository
) : ViewModel() {
    
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()
    
    private val _selectedMood = MutableStateFlow<MoodType?>(null)
    val selectedMood: StateFlow<MoodType?> = _selectedMood.asStateFlow()
    
    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content.asStateFlow()
    
    private val _selectedTags = MutableStateFlow<Set<String>>(emptySet())
    val selectedTags: StateFlow<Set<String>> = _selectedTags.asStateFlow()
    
    private val _currentEntry = MutableStateFlow<DiaryEntry?>(null)
    val currentEntry: StateFlow<DiaryEntry?> = _currentEntry.asStateFlow()
    
    fun setDate(date: LocalDate) {
        _selectedDate.value = date
        loadEntryForDate(date)
    }
    
    fun setMood(mood: MoodType) {
        _selectedMood.value = mood
    }
    
    fun setContent(text: String) {
        _content.value = text
    }
    
    fun toggleTag(tag: String) {
        val currentTags = _selectedTags.value.toMutableSet()
        if (currentTags.contains(tag)) {
            currentTags.remove(tag)
        } else {
            currentTags.add(tag)
        }
        _selectedTags.value = currentTags
    }
    
    private fun loadEntryForDate(date: LocalDate) {
        viewModelScope.launch {
            val entry = repository.getByDate(date)
            _currentEntry.value = entry
            entry?.let {
                _selectedMood.value = it.mood
                _content.value = it.content
                _selectedTags.value = it.tags.toSet()
            }
        }
    }
    
    suspend fun saveEntry(): Boolean {
        val mood = _selectedMood.value ?: return false
        val content = _content.value
        
        if (content.length > 500) {
            return false
        }
        
        val entry = DiaryEntry(
            id = _currentEntry.value?.id ?: 0,
            date = _selectedDate.value,
            mood = mood,
            content = content,
            tags = _selectedTags.value.toList()
        )
        
        repository.insertOrUpdate(entry)
        return true
    }
    
    suspend fun deleteEntry() {
        _currentEntry.value?.let { entry ->
            repository.delete(entry)
            resetForm()
        }
    }
    
    private fun resetForm() {
        _selectedMood.value = null
        _content.value = ""
        _selectedTags.value = emptySet()
        _currentEntry.value = null
    }
}