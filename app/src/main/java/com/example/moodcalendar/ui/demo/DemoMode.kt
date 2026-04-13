package com.example.moodcalendar.ui.demo

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.moodcalendar.data.demo.DemoData
import com.example.moodcalendar.data.repository.DiaryRepository
import kotlinx.coroutines.launch

@Composable
fun DemoModeDialog(
    repository: DiaryRepository,
    onDismiss: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var isCompleted by remember { mutableStateOf(false) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isCompleted) "演示数据已加载" else "体验演示模式",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                if (isLoading) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("正在加载演示数据...")
                } else if (isCompleted) {
                    Text(
                        text = "已为您添加了7天的演示心情记录！\n\n现在您可以：\n• 查看日历上的心情标记\n• 浏览日记列表\n• 体验统计功能\n• 测试搜索和筛选",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(onClick = onDismiss) {
                        Text("开始体验")
                    }
                } else {
                    Text(
                        text = "这个演示模式会为您添加一些示例数据，让您能立即看到应用的各种功能效果。",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = {
                            isLoading = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "开始")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("加载演示数据")
                    }
                }
            }
        }
    }
}