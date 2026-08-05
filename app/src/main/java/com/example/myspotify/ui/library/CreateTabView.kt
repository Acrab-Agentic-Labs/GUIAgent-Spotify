package com.example.myspotify.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * CreateTab - 创建弹窗（底部弹出）
 * 从 YourLibraryTab 右上角 "+" 进入
 */
@Composable
fun CreateTabView(
    onDismiss: () -> Unit,
    onPlaylistCreate: () -> Unit = {}
) {
    // 半透明背景遮罩
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.BottomCenter
    ) {
        // 底部弹窗内容
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Color(0xFF282828))
                .clickable(enabled = false, onClick = {}) // 防止点击穿透
                .padding(top = 24.dp, bottom = 16.dp)
        ) {
            // 标题
            Text(
                text = "Create",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Playlist 选项
            CreateOptionItem(
                icon = Icons.Default.MusicNote,
                title = "Playlist",
                description = "Build a playlist with songs, or episodes",
                onClick = {
                    onDismiss()
                    onPlaylistCreate()
                }
            )

            // Blend 选项
            CreateOptionItem(
                icon = Icons.Default.People,
                title = "Blend",
                description = "Combine tastes with friends to make a shared playlist",
                onClick = { onDismiss() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Cancel 按钮
            TextButton(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Cancel",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * 创建选项项
 */
@Composable
private fun CreateOptionItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF3E3E3E)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}
