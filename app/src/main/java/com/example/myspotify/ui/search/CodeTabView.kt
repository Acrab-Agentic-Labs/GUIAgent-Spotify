package com.example.myspotify.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * CodeTab View - Spotify Code 扫描页面
 */
@Composable
fun CodeTabView(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 顶部栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        // 中间扫码区域
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 扫码框
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1A1A1A))
                    .border(2.dp, Color(0xFF535353), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                // 内部扫描区域
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 提示文字
            Text(
                text = "Scan Spotify Code",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Point your camera at a\nSpotify Code to play a song,\nalbum, playlist, and more.",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}
