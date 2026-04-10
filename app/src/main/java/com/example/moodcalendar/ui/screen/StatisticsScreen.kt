package com.example.moodcalendar.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moodcalendar.data.model.MoodType
import com.example.moodcalendar.ui.viewmodel.StatisticsViewModel
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(navController: NavController) {
    val viewModel: StatisticsViewModel = hiltViewModel()
    val statistics by viewModel.statistics.collectAsState()
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("心情统计") },
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
                .verticalScroll(rememberScrollState())
        ) {
            // 月份导航
            PeriodNavigation(
                selectedPeriod = selectedPeriod,
                onPreviousPeriod = { 
                    viewModel.setPeriod(selectedPeriod.minusMonths(1)) 
                },
                onNextPeriod = { 
                    viewModel.setPeriod(selectedPeriod.plusMonths(1)) 
                }
            )
            
            // 连续记录天数
            ConsecutiveDaysCard(consecutiveDays = statistics.consecutiveDays)
            
            // 心情分布统计
            MoodDistributionCard(moodDistribution = statistics.moodDistribution)
            
            // 心情趋势
            MoodTrendCard(moodTrend = statistics.moodTrend)
            
            // 标签统计
            TagStatisticsCard(tagStatistics = statistics.tagStatistics)
        }
    }
}

@Composable
fun PeriodNavigation(
    selectedPeriod: YearMonth,
    onPreviousPeriod: () -> Unit,
    onNextPeriod: () -> Unit
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousPeriod) {
                Icon(Icons.Default.ArrowLeft, contentDescription = "上个月")
            }
            
            Text(
                text = selectedPeriod.format(DateTimeFormatter.ofPattern("yyyy年MM月")),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            IconButton(onClick = onNextPeriod) {
                Icon(Icons.Default.ArrowRight, contentDescription = "下个月")
            }
        }
    }
}

@Composable
fun ConsecutiveDaysCard(consecutiveDays: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "连续记录",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "$consecutiveDays 天",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "继续坚持记录心情吧！",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun MoodDistributionCard(moodDistribution: Map<MoodType, Int>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "心情分布",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            moodDistribution.entries.sortedByDescending { it.value }.forEach { (mood, count) ->
                MoodDistributionItem(mood = mood, count = count)
            }
        }
    }
}

@Composable
fun MoodDistributionItem(mood: MoodType, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = mood.emoji,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = mood.displayName,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        Text(
            text = "$count 次",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MoodTrendCard(moodTrend: List<Pair<java.time.LocalDate, Int>>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "心情趋势（近30天）",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 简单的趋势展示（由于Vico图表需要更复杂的配置）
            if (moodTrend.isNotEmpty()) {
                val averageMood = moodTrend.filter { it.second > 0 }.map { it.second }.average()
                val maxMood = moodTrend.maxOfOrNull { it.second } ?: 0
                val minMood = moodTrend.filter { it.second > 0 }.minOfOrNull { it.second } ?: 0
                
                Column {
                    Text(
                        text = "平均心情值: ${String.format("%.1f", averageMood)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "最高: $maxMood, 最低: $minMood",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // 简单的趋势可视化
                    SimpleTrendVisualization(moodTrend)
                }
            } else {
                Text(
                    text = "暂无数据",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SimpleTrendVisualization(moodTrend: List<Pair<java.time.LocalDate, Int>>) {
    val validData = moodTrend.filter { it.second > 0 }
    
    if (validData.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            // 简单的趋势线展示
            Text(
                text = "图表功能需要Vico图表库支持",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        Text(
            text = "暂无有效数据",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TagStatisticsCard(tagStatistics: Map<String, Int>) {
    if (tagStatistics.isNotEmpty()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "标签使用统计",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                tagStatistics.entries.sortedByDescending { it.value }.forEach { (tag, count) ->
                    TagStatisticsItem(tag = tag, count = count)
                }
            }
        }
    }
}

@Composable
fun TagStatisticsItem(tag: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = tag,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Text(
            text = "$count 次",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}