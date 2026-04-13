package com.example.moodcalendar.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moodcalendar.data.model.MoodType
import com.example.moodcalendar.ui.viewmodel.MoodEntryViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodEntryScreen(navController: NavController) {
    val viewModel: MoodEntryViewModel = hiltViewModel()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedMood by viewModel.selectedMood.collectAsState()
    val content by viewModel.content.collectAsState()
    val selectedTags by viewModel.selectedTags.collectAsState()
    val currentEntry by viewModel.currentEntry.collectAsState()
    
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("记录心情") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    if (currentEntry != null) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.deleteEntry()
                                    navController.popBackStack()
                                }
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "删除")
                        }
                    }
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                if (viewModel.saveEntry()) {
                                    navController.popBackStack()
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "保存")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // 日期显示
            DateDisplay(selectedDate)
            
            // 心情选择
            MoodSelection(
                selectedMood = selectedMood,
                onMoodSelected = { viewModel.setMood(it) }
            )
            
            // 日记输入
            DiaryInput(
                content = content,
                onContentChange = { viewModel.setContent(it) }
            )
            
            // 标签选择
            TagSelection(
                selectedTags = selectedTags,
                onTagToggled = { viewModel.toggleTag(it) }
            )
        }
    }
}

@Composable
fun DateDisplay(date: java.time.LocalDate) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "日期",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = date.format(java.time.format.DateTimeFormatter.ofPattern("yyyy年MM月dd日")),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MoodSelection(
    selectedMood: MoodType?,
    onMoodSelected: (MoodType) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "选择心情",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MoodType.values().forEach { mood ->
                    MoodOption(
                        mood = mood,
                        isSelected = selectedMood == mood,
                        onSelected = { onMoodSelected(mood) }
                    )
                }
            }
        }
    }
}

@Composable
fun MoodOption(
    mood: MoodType,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    
    Card(
        modifier = Modifier
            .width(60.dp)
            .height(80.dp),
        onClick = onSelected,
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = mood.emoji,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = mood.displayName,
                style = MaterialTheme.typography.labelSmall,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun DiaryInput(
    content: String,
    onContentChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "日记内容",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = content,
                onValueChange = onContentChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                placeholder = { Text("写下今天的心情...") },
                singleLine = false,
                maxLines = 10
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "字数: ${content.length}/500",
                style = MaterialTheme.typography.labelSmall,
                color = if (content.length > 500) MaterialTheme.colorScheme.error 
                       else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun TagSelection(
    selectedTags: Set<String>,
    onTagToggled: (String) -> Unit
) {
    val defaultTags = listOf("工作", "学习", "运动", "社交", "休息", "其他")
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "选择标签",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                defaultTags.forEach { tag ->
                    TagChip(
                        tag = tag,
                        isSelected = selectedTags.contains(tag),
                        onToggle = { onTagToggled(tag) }
                    )
                }
            }
        }
    }
}

@Composable
fun TagChip(
    tag: String,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onToggle,
        label = { Text(tag) }
    )
}