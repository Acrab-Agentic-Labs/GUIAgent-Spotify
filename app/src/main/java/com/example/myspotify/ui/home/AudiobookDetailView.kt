package com.example.myspotify.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.ui.common.AssetImage

/**
 * AudiobookDetailView - 有声书详情页
 * 点击 AudiobooksTab 中的有声书卡片进入
 */
@Composable
fun AudiobookDetailView(
    audiobookItem: AudiobookFeedItem,
    onBack: () -> Unit,
    isSaved: Boolean = false,
    onToggleSave: () -> Unit = {}
) {
    var progress by remember { mutableFloatStateOf(0f) }
    var isPlaying by remember { mutableStateOf(false) }
    val totalDuration = "5h 32min"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
        // 顶部返回按钮
        IconButton(
            onClick = onBack,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        // 书籍封面
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp),
            contentAlignment = Alignment.Center
        ) {
            AssetImage(
                assetPath = audiobookItem.thumbnailAsset,
                contentDescription = audiobookItem.title,
                modifier = Modifier
                    .size(220.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 书名 + 作者
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = audiobookItem.title,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "by ${audiobookItem.author}",
                color = Color.Gray,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = totalDuration,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 进度条
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Slider(
                value = progress,
                onValueChange = { progress = it },
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color(0xFF535353)
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 播放控制
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                progress = (progress - 0.05f).coerceAtLeast(0f)
            }) {
                Icon(
                    imageVector = Icons.Default.Replay,
                    contentDescription = "Rewind",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1DB954))
                    .clickable { isPlaying = !isPlaying },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = Color.Black,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            IconButton(onClick = {
                progress = (progress + 0.05f).coerceAtMost(1f)
            }) {
                Icon(
                    imageVector = Icons.Default.Forward10,
                    contentDescription = "Forward",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            // 收藏按钮
            IconButton(onClick = onToggleSave) {
                Icon(
                    imageVector = if (isSaved) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = if (isSaved) "Saved" else "Save",
                    tint = if (isSaved) Color(0xFF1DB954) else Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(
            color = Color(0xFF535353),
            thickness = 0.5.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // About this book 标题
        Text(
            text = "About this book",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 书籍介绍
        val bookDescription = remember(audiobookItem.title) {
            when {
                audiobookItem.title.contains("Reading", ignoreCase = true) ->
                    "In \"The Art of Reading,\" James Clear takes readers on a transformative journey through the world of books and reading habits. " +
                    "Drawing from scientific research and personal experience, Clear reveals how the simple act of reading can reshape our minds, " +
                    "expand our perspectives, and fundamentally change the way we approach life.\n\n" +
                    "The book explores practical strategies for building a consistent reading practice, from choosing the right books to creating " +
                    "an environment that supports deep focus. Clear argues that reading is not just a passive activity but an active engagement " +
                    "with ideas that can spark creativity and innovation.\n\n" +
                    "Whether you're a lifelong reader looking to deepen your practice or someone who has struggled to maintain a reading habit, " +
                    "this audiobook offers actionable insights and inspiring stories that will motivate you to pick up your next great book."

                audiobookItem.title.contains("Stories", ignoreCase = true) ->
                    "Sarah Johnson's \"Stories That Matter\" is a compelling exploration of the narratives that have shaped our modern world. " +
                    "From groundbreaking investigative journalism to personal memoirs that changed public opinion, Johnson examines how storytelling " +
                    "remains the most powerful tool for understanding the human experience.\n\n" +
                    "Each chapter focuses on a different genre of impactful storytelling, analyzing what makes certain stories resonate across " +
                    "cultures and generations. Johnson interviews authors, journalists, and filmmakers to uncover the craft behind stories " +
                    "that move people to action.\n\n" +
                    "This audiobook is both a celebration of great storytelling and a practical guide for anyone who wants to tell stories " +
                    "that truly matter. It will change the way you think about the narratives that surround us every day."

                else ->
                    "David Park's \"Mindful Listening\" is a groundbreaking guide to developing a deeper relationship with sound. " +
                    "In a world filled with noise, Park teaches readers how to cultivate the art of truly listening — not just hearing, " +
                    "but actively engaging with the sounds around us.\n\n" +
                    "The book combines meditation techniques with music theory to create a unique approach to auditory awareness. " +
                    "Park draws on his experience as both a musician and a meditation teacher to show how mindful listening can " +
                    "reduce stress, improve relationships, and unlock new dimensions of musical appreciation.\n\n" +
                    "Featuring guided listening exercises and real-world examples, this audiobook provides a practical toolkit for " +
                    "anyone who wants to experience the world through more attentive ears. Perfect for music lovers, meditation " +
                    "practitioners, and anyone seeking a deeper connection with their environment."
            }
        }

        Text(
            text = bookDescription,
            color = Color(0xFFAAAAAA),
            fontSize = 15.sp,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 底部间距
        Spacer(modifier = Modifier.height(100.dp))
    }
}
