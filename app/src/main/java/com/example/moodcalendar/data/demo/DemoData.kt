package com.example.moodcalendar.data.demo

import com.example.moodcalendar.data.model.DiaryEntry
import com.example.moodcalendar.data.model.MoodType
import java.time.LocalDate

object DemoData {
    
    fun generateDemoEntries(): List<DiaryEntry> {
        return listOf(
            DiaryEntry(
                date = LocalDate.now().minusDays(1),
                mood = MoodType.HAPPY,
                content = "今天项目顺利完成，团队合作很愉快！学到了很多新技术。",
                tags = listOf("工作", "学习")
            ),
            DiaryEntry(
                date = LocalDate.now().minusDays(2),
                mood = MoodType.GOOD,
                content = "和朋友一起吃了顿美味的晚餐，聊了很多有趣的话题。",
                tags = listOf("社交", "美食")
            ),
            DiaryEntry(
                date = LocalDate.now().minusDays(3),
                mood = MoodType.NEUTRAL,
                content = "平静的一天，完成了日常任务，晚上看了会书。",
                tags = listOf("学习", "休息")
            ),
            DiaryEntry(
                date = LocalDate.now().minusDays(4),
                mood = MoodType.SAD,
                content = "遇到了一些技术难题，调试了很久才解决，有点疲惫。",
                tags = listOf("工作", "学习")
            ),
            DiaryEntry(
                date = LocalDate.now().minusDays(5),
                mood = MoodType.VERY_SAD,
                content = "今天心情不太好，很多事情都不顺利，需要调整心态。",
                tags = listOf("休息")
            ),
            DiaryEntry(
                date = LocalDate.now().minusDays(7),
                mood = MoodType.GOOD,
                content = "周末去公园散步，呼吸新鲜空气，心情变得很好。",
                tags = listOf("运动", "休息")
            )
        )
    }
}