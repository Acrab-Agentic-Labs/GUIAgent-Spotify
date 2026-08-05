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
 * Podcasts 标签页内容 — 视频播客 Feed 流
 */
fun LazyListScope.podcastsContent(onPodcastClick: (PodcastFeedItem) -> Unit = {}) {
    val podcastItems = listOf(
        PodcastFeedItem(
            title = "How AI is Changing Music",
            source = "The Daily",
            date = "Dec 15",
            previewAsset = "video_cover/1.jpg",
            thumbnailAsset = "video_cover/1.jpg"
        ),
        PodcastFeedItem(
            title = "Behind the Lyrics",
            source = "Music Stories",
            date = "Dec 12",
            previewAsset = "video_cover/2.jpg",
            thumbnailAsset = "video_cover/2.jpg"
        ),
        PodcastFeedItem(
            title = "The Sound of Tomorrow",
            source = "Audio Lab",
            date = "Dec 8",
            previewAsset = "video_cover/3.jpg",
            thumbnailAsset = "video_cover/3.jpg"
        )
    )

    podcastItems.forEach { feedItem ->
        item {
            PodcastVideoCard(feedItem, onClick = { onPodcastClick(feedItem) })
        }
    }

    // 底部间距
    item {
        Spacer(modifier = Modifier.height(100.dp))
    }
}

/**
 * 播客 Feed 项数据
 */
data class PodcastFeedItem(
    val title: String,
    val source: String,
    val date: String,
    val previewAsset: String,
    val thumbnailAsset: String
)

/**
 * 视频播客卡片
 */
@Composable
fun PodcastVideoCard(item: PodcastFeedItem, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // 大尺寸预览图区域（16:9 比例，带视频控制按钮）
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF282828))
        ) {
            // 预览图（暂用占位，后续替换为 AssetImage）
            if (item.previewAsset.isNotEmpty()) {
                com.example.myspotify.ui.common.AssetImage(
                    assetPath = item.previewAsset,
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // 右下角视频控制按钮
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.Black.copy(alpha = 0.6f))
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Volume",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.Black.copy(alpha = 0.6f))
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

        Spacer(modifier = Modifier.height(12.dp))

        // 标题
        Text(
            text = item.title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 底部信息行：缩略图 + 来源 + 时间
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 小缩略图（占位）
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFF383838))
            ) {
                if (item.thumbnailAsset.isNotEmpty()) {
                    com.example.myspotify.ui.common.AssetImage(
                        assetPath = item.thumbnailAsset,
                        contentDescription = item.source,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "${item.source} · ${item.date}",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}
