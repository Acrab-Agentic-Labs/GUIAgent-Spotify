package com.example.myspotify.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ImportYourMusicTab - 导入音乐页面
 * 从 YourLibraryTab 的 "Import your music from other apps" 进入
 */
@Composable
fun ImportYourMusicTabView(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 顶部栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Import your library",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 居中内容
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 说明文字
            Text(
                text = "Import playlists, songs, and albums from another music service to your Spotify library.",
                color = Color.Gray,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // 大图标占位区域
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF282828)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FileDownload,
                    contentDescription = "Import",
                    tint = Color.Gray,
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Import 按钮
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1DB954)
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Import",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Learn more 链接
            Text(
                text = "Learn more",
                color = Color(0xFF1DB954),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            // 把内容推向上方的间距
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
