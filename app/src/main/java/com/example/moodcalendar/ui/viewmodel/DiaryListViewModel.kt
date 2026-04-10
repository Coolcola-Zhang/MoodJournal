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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DiaryListState(
    val entries: List<DiaryEntry> = emptyList(),
    val searchQuery: String = "",
    val selectedMood: MoodType? = null,
    val selectedTag: String? = null,
    val filteredEntries: List<DiaryEntry> = emptyList()
)

@HiltViewModel
class DiaryListViewModel @Inject constructor(
    private val repository: DiaryRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(DiaryListState())
    val state: StateFlow<DiaryListState> = _state.asStateFlow()
    
    init {
        loadDiaries()
    }
    
    private fun loadDiaries() {
        viewModelScope.launch {
            repository.getAll().collect { entries ->
                val filteredEntries = applyFilters(entries)
                _state.value = _state.value.copy(
                    entries = entries,
                    filteredEntries = filteredEntries
                )
            }
        }
    }
    
    fun setSearchQuery(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        applyFilters()
    }
    
    fun setSelectedMood(mood: MoodType?) {
        _state.value = _state.value.copy(selectedMood = mood)
        applyFilters()
    }
    
    fun setSelectedTag(tag: String?) {
        _state.value = _state.value.copy(selectedTag = tag)
        applyFilters()
    }
    
    private fun applyFilters() {
        viewModelScope.launch {
            val currentEntries = _state.value.entries
            val filteredEntries = applyFilters(currentEntries)
            _state.value = _state.value.copy(filteredEntries = filteredEntries)
        }
    }
    
    private suspend fun applyFilters(entries: List<DiaryEntry>): List<DiaryEntry> {
        var filtered = entries
        
        // 搜索过滤
        val searchQuery = _state.value.searchQuery
        if (searchQuery.isNotBlank()) {
            filtered = repository.search(searchQuery).map { it }
        }
        
        // 心情过滤
        val selectedMood = _state.value.selectedMood
        if (selectedMood != null) {
            filtered = repository.getByMood(selectedMood.value).map { it }
        }
        
        // 标签过滤
        val selectedTag = _state.value.selectedTag
        if (selectedTag != null) {
            filtered = repository.getByTag(selectedTag).map { it }
        }
        
        return filtered
    }
    
    fun clearFilters() {
        _state.value = _state.value.copy(
            searchQuery = "",
            selectedMood = null,
            selectedTag = null
        )
        applyFilters()
    }
}