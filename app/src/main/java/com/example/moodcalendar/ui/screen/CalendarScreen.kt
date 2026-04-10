package com.example.moodcalendar.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moodcalendar.data.model.MoodType
import com.example.moodcalendar.ui.navigation.Screen
import com.example.moodcalendar.ui.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(navController: NavController) {
    val viewModel: CalendarViewModel = hiltViewModel()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val entries by viewModel.entries.collectAsState()
    
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("心情日记") },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Statistics.route) }) {
                        Icon(Icons.Default.PieChart, contentDescription = "统计")
                    }
                    IconButton(onClick = { navController.navigate(Screen.DiaryList.route) }) {
                        Icon(Icons.Default.List, contentDescription = "日记列表")
                    }
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Default.Settings, contentDescription = "设置")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.MoodEntry.route) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加心情")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // 月份导航
            MonthNavigation(
                currentMonth = currentMonth,
                onPreviousMonth = { currentMonth = currentMonth.minusMonths(1) },
                onNextMonth = { currentMonth = currentMonth.plusMonths(1) }
            )
            
            // 星期标题
            WeekHeader()
            
            // 日历网格
            CalendarGrid(
                currentMonth = currentMonth,
                entries = entries,
                selectedDate = selectedDate,
                onDateClick = { date ->
                    viewModel.selectDate(date)
                    navController.navigate(Screen.MoodEntry.route)
                }
            )
        }
    }
}

@Composable
fun MonthNavigation(
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "上个月"
            )
        }
        
        Text(
            text = currentMonth.format(DateTimeFormatter.ofPattern("yyyy年MM月")),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        IconButton(onClick = onNextMonth) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "下个月"
            )
        }
    }
}

@Composable
fun WeekHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        val weekDays = listOf("日", "一", "二", "三", "四", "五", "六")
        weekDays.forEach { day ->
            Text(
                text = day,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun CalendarGrid(
    currentMonth: YearMonth,
    entries: List<com.example.moodcalendar.data.model.DiaryEntry>,
    selectedDate: LocalDate,
    onDateClick: (LocalDate) -> Unit
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val daysInMonth = currentMonth.lengthOfMonth()
    val startOffset = firstDayOfMonth.dayOfWeek.value % 7
    
    val days = List(42) { index ->
        val dayOffset = index - startOffset
        if (dayOffset >= 0 && dayOffset < daysInMonth) {
            currentMonth.atDay(dayOffset + 1)
        } else {
            null
        }
    }
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(days) { date ->
            CalendarDay(
                date = date,
                entries = entries,
                selectedDate = selectedDate,
                onDateClick = onDateClick
            )
        }
    }
}

@Composable
fun CalendarDay(
    date: LocalDate?,
    entries: List<com.example.moodcalendar.data.model.DiaryEntry>,
    selectedDate: LocalDate,
    onDateClick: (LocalDate) -> Unit
) {
    if (date == null) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .padding(4.dp)
        )
        return
    }
    
    val entry = entries.find { it.date == date }
    val isToday = date == LocalDate.now()
    val isSelected = date == selectedDate
    
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp),
        onClick = { onDateClick(date) },
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> MaterialTheme.colorScheme.primaryContainer
                isToday -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                )
                
                if (entry != null) {
                    MoodIndicator(mood = entry.mood)
                }
            }
        }
    }
}

@Composable
fun MoodIndicator(mood: MoodType) {
    Text(
        text = mood.emoji,
        style = MaterialTheme.typography.bodySmall
    )
}