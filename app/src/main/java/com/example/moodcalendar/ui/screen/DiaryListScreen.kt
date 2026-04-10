package com.example.moodcalendar.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.FlowRow as ComposeFlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moodcalendar.data.model.MoodType
import com.example.moodcalendar.ui.viewmodel.DiaryListViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryListScreen(navController: NavController) {
    val viewModel: DiaryListViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("日记列表") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // 搜索栏
            SearchBar(
                query = state.searchQuery,
                onQueryChange = { viewModel.setSearchQuery(it) },
                onClearQuery = { viewModel.setSearchQuery("") }
            )
            
            // 筛选栏
            FilterBar(
                selectedMood = state.selectedMood,
                selectedTag = state.selectedTag,
                onMoodSelected = { viewModel.setSelectedMood(it) },
                onTagSelected = { viewModel.setSelectedTag(it) },
                onClearFilters = { viewModel.clearFilters() }
            )
            
            // 日记列表
            DiaryList(
                entries = state.filteredEntries,
                onEntryClick = { /* TODO: 跳转到详情页 */ }
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "搜索",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("搜索日记内容...") },
                singleLine = true,
                trailingIcon = {
                    if (query.isNotBlank()) {
                        IconButton(onClick = onClearQuery) {
                            Icon(Icons.Default.Clear, contentDescription = "清除")
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun FilterBar(
    selectedMood: MoodType?,
    selectedTag: String?,
    onMoodSelected: (MoodType?) -> Unit,
    onTagSelected: (String?) -> Unit,
    onClearFilters: () -> Unit
) {
    val defaultTags = listOf("工作", "学习", "运动", "社交", "休息", "其他")
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "筛选",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                if (selectedMood != null || selectedTag != null) {
                    TextButton(onClick = onClearFilters) {
                        Text("清除筛选")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 心情筛选
            Text(
                text = "心情",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MoodType.values().forEach { mood ->
                    FilterChip(
                        selected = selectedMood == mood,
                        onClick = { 
                            onMoodSelected(if (selectedMood == mood) null else mood) 
                        },
                        label = { 
                            Text("${mood.emoji} ${mood.displayName}") 
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 标签筛选
            Text(
                text = "标签",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            ComposeFlowRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                defaultTags.forEach { tag ->
                    FilterChip(
                        selected = selectedTag == tag,
                        onClick = { 
                            onTagSelected(if (selectedTag == tag) null else tag) 
                        },
                        label = { Text(tag) }
                    )
                }
            }
        }
    }
}

@Composable
fun DiaryList(
    entries: List<com.example.moodcalendar.data.model.DiaryEntry>,
    onEntryClick: (com.example.moodcalendar.data.model.DiaryEntry) -> Unit
) {
    if (entries.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "暂无日记记录",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(entries) { entry ->
                DiaryCard(
                    entry = entry,
                    onClick = { onEntryClick(entry) }
                )
            }
        }
    }
}

@Composable
fun DiaryCard(
    entry: com.example.moodcalendar.data.model.DiaryEntry,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.date.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    text = "${entry.mood.emoji} ${entry.mood.displayName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            if (entry.content.isNotBlank()) {
                Text(
                    text = if (entry.content.length > 100) 
                        "${entry.content.substring(0, 100)}..." 
                    else entry.content,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            if (entry.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                
                ComposeFlowRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    entry.tags.forEach { tag ->
                        SuggestionChip(
                            onClick = { },
                            label = { Text(tag) }
                        )
                    }
                }
            }
        }
    }
}