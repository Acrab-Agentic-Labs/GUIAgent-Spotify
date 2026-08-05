package com.example.myspotify.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Audiobooks 标签页内容 — 有声书卡片 Feed 流
 */
fun LazyListScope.audiobooksContent(onAudiobookClick: (AudiobookFeedItem) -> Unit = {}) {
    val audiobookItems = listOf(
        AudiobookFeedItem(
            title = "The Art of Reading",
            author = "James Clear",
            description = "A guide to building better reading habits and finding joy in every page. Learn how to make reading a daily practice that transforms your life.",
            thumbnailAsset = "audiobook_cover/1.png"
        ),
        AudiobookFeedItem(
            title = "Stories That Matter",
            author = "Sarah Johnson",
            description = "Exploring the most impactful stories of our generation and their lasting influence on culture, society, and the way we see the world.",
            thumbnailAsset = "audiobook_cover/2.png"
        ),
        AudiobookFeedItem(
            title = "Mindful Listening",
            author = "David Park",
            description = "Discover the power of mindful listening and how it can deepen your understanding of music, conversations, and the world around you.",
            thumbnailAsset = "audiobook_cover/3.png"
        )
    )

    audiobookItems.forEach { item ->
        item {
            AudiobookCard(item, onClick = { onAudiobookClick(item) })
        }
    }

    // 底部间距
    item {
        Spacer(modifier = Modifier.height(100.dp))
    }
}

/**
 * 有声书 Feed 项数据
 */
data class AudiobookFeedItem(
    val title: String,
    val author: String,
    val description: String,
    val thumbnailAsset: String
)

/**
 * 有声书卡片
 */
@Composable
fun AudiobookCard(item: AudiobookFeedItem, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1A1A1A))
            .padding(16.dp)
    ) {
        // 上部：缩略图 + 书名/作者
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // 左上角缩略图（占位）
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF383838))
            ) {
                if (item.thumbnailAsset.isNotEmpty()) {
                    com.example.myspotify.ui.common.AssetImage(
                        assetPath = item.thumbnailAsset,
                        contentDescription = item.title,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 右侧书名和作者
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.author,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 内容介绍
        Text(
            text = item.description,
            color = Color(0xFFAAAAAA),
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 右下角按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF333333))
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Volume",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF333333))
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
