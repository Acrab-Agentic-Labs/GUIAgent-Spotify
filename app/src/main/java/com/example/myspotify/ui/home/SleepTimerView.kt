package com.example.myspotify.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * SleepTimerView - 睡眠定时器选择页面
 * 从 MenuTab 的 Sleep timer 进入
 */
@Composable
fun SleepTimerView(
    isTimerActive: Boolean,
    onBack: () -> Unit,
    onSelectDuration: (String) -> Unit,
    onTurnOff: () -> Unit
) {
    val durations = listOf(
        "5 minutes",
        "10 minutes",
        "15 minutes",
        "30 minutes",
        "45 minutes",
        "1 hour",
        "End of track"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF282828))
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // 顶部栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Sleep timer",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Turn off timer 选项（仅在定时器激活时显示）
        if (isTimerActive) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onTurnOff)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TimerOff,
                    contentDescription = "Turn off",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Turn off timer",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            HorizontalDivider(
                color = Color(0xFF535353),
                thickness = 0.5.dp,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )
        }

        // 时长选项列表
        durations.forEach { duration ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectDuration(duration) }
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = duration,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}
