package com.example.myspotify.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.model.Song

/**
 * LyricsTab View - 全屏歌词页面
 */
@Composable
fun LyricsTabView(song: Song? = null, onBack: () -> Unit) {
    val songTitle = song?.title ?: "IRIS OUT"
    val songArtist = song?.artistName ?: "Kenshi Yonezu"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3D5A80),
                        Color(0xFF2C3E50),
                        Color(0xFF1A1A2E)
                    )
                )
            )
    ) {
        // 顶部栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = songTitle,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = songArtist,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White
                )
            }
        }

        // 歌词内容
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            LyricLine(text = "I close my eyes and see", alpha = 0.4f)
            LyricLine(text = "A world that's meant for you and me", alpha = 0.4f)
            LyricLine(text = "The colors bloom beneath the light", alpha = 0.4f)
            LyricLine(text = "", alpha = 0f)
            LyricLine(text = "We dance along the edge of time", alpha = 1.0f)
            LyricLine(text = "Your voice like bells that softly chime", alpha = 0.7f)
            LyricLine(text = "Through every storm you hold my hand", alpha = 0.5f)
            LyricLine(text = "", alpha = 0f)
            LyricLine(text = "The iris blooms in morning dew", alpha = 0.4f)
            LyricLine(text = "A thousand colors, every hue", alpha = 0.4f)
            LyricLine(text = "I find my way back home to you", alpha = 0.4f)
            LyricLine(text = "", alpha = 0f)
            LyricLine(text = "So take my hand and don't let go", alpha = 0.4f)
            LyricLine(text = "Through fields of gold and falling snow", alpha = 0.4f)
            LyricLine(text = "The world is ours, this much I know", alpha = 0.4f)

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

/**
 * 歌词行组件
 */
@Composable
private fun LyricLine(text: String, alpha: Float) {
    if (text.isEmpty()) {
        Spacer(modifier = Modifier.height(24.dp))
        return
    }
    Text(
        text = text,
        color = Color.White.copy(alpha = alpha),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 6.dp)
    )
}
