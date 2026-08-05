package com.example.myspotify.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.ui.common.AssetImage

/**
 * 评论数据
 */
data class Comment(
    val id: String,
    val username: String,
    val avatarAsset: String,
    val text: String,
    val timeAgo: String,
    val likes: Int = 0
)

/**
 * PodcastDetailView - 播客视频详情页
 * 点击 PodcastsTab 中的视频卡片进入
 */
@Composable
fun PodcastDetailView(
    podcastItem: PodcastFeedItem,
    onBack: () -> Unit,
    isSaved: Boolean = false,
    onToggleSave: () -> Unit = {}
) {
    // 进度条状态
    var progress by remember { mutableFloatStateOf(0.3f) }
    val totalDuration = "32:15"
    val currentTime = remember(progress) {
        val totalSeconds = 32 * 60 + 15
        val currentSeconds = (totalSeconds * progress).toInt()
        val min = currentSeconds / 60
        val sec = currentSeconds % 60
        "%d:%02d".format(min, sec)
    }
    var isPlaying by remember { mutableStateOf(false) }

    // 评论数据
    val initialComments = remember {
        listOf(
            Comment("c1", "MusicLover92", "avatar/1.png", "This episode really opened my eyes to how AI is reshaping the music industry. Great insights!", "2 days ago", 24),
            Comment("c2", "TechGeek", "avatar/2.png", "Fascinating discussion! Would love to hear more about the ethical implications.", "3 days ago", 18),
            Comment("c3", "SoundWave", "avatar/3.png", "The part about AI-generated melodies was mind-blowing. Shared this with all my musician friends.", "5 days ago", 31),
            Comment("c4", "PodcastFan", "avatar/4.png", "Best episode yet! Keep up the amazing work.", "1 week ago", 12),
            Comment("c5", "AudioNerd", "avatar/5.png", "I disagree with some points but it was still a very thought-provoking listen.", "1 week ago", 7)
        )
    }
    val comments = remember { mutableStateListOf(*initialComments.toTypedArray()) }
    var commentInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            // 顶部返回按钮
            item {
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
            }

            // 视频预览图
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF282828))
                ) {
                    AssetImage(
                        assetPath = podcastItem.previewAsset,
                        contentDescription = podcastItem.title,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // 进度条
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = currentTime, color = Color.Gray, fontSize = 12.sp)
                        Text(text = totalDuration, color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }

            // 播放控制按钮
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 后退 15 秒
                    IconButton(onClick = {
                        progress = (progress - 15f / (32 * 60 + 15)).coerceAtLeast(0f)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = "Rewind 15s",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(24.dp))

                    // 播放/暂停
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

                    // 前进 15 秒
                    IconButton(onClick = {
                        progress = (progress + 15f / (32 * 60 + 15)).coerceAtMost(1f)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Forward10,
                            contentDescription = "Forward 15s",
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
            }

            // 标题 + 来源信息
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = podcastItem.title,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF383838))
                        ) {
                            AssetImage(
                                assetPath = podcastItem.thumbnailAsset,
                                contentDescription = podcastItem.source,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${podcastItem.source} · ${podcastItem.date}",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // 分隔线
            item {
                HorizontalDivider(
                    color = Color(0xFF535353),
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            // 评论区标题
            item {
                Text(
                    text = "Comments · ${comments.size}",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // 评论列表
            items(comments, key = { it.id }) { comment ->
                CommentItem(comment = comment)
            }

            // 底部间距
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // 底部评论输入栏
        HorizontalDivider(color = Color(0xFF535353), thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A1A1A))
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 用户头像
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF5722)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 输入框
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF282828))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (commentInput.isEmpty()) {
                    Text(
                        text = "Add a comment...",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
                BasicTextField(
                    value = commentInput,
                    onValueChange = { commentInput = it },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = Color.White,
                        fontSize = 14.sp
                    ),
                    cursorBrush = SolidColor(Color.White),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 发送按钮
            IconButton(
                onClick = {
                    if (commentInput.isNotBlank()) {
                        val newComment = Comment(
                            id = "c${comments.size + 1}_${System.currentTimeMillis()}",
                            username = "You",
                            avatarAsset = "",
                            text = commentInput.trim(),
                            timeAgo = "Just now",
                            likes = 0
                        )
                        comments.add(0, newComment)
                        commentInput = ""
                    }
                },
                enabled = commentInput.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = if (commentInput.isNotBlank()) Color(0xFF1DB954) else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

/**
 * 评论项组件
 */
@Composable
private fun CommentItem(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        crossAxisAlignment = Alignment.Top
    ) {
        // 用户头像
        if (comment.avatarAsset.isNotEmpty()) {
            AssetImage(
                assetPath = comment.avatarAsset,
                contentDescription = comment.username,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF5722)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = comment.username,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            // 用户名 + 时间
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = comment.username,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = comment.timeAgo,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 评论内容
            Text(
                text = comment.text,
                color = Color(0xFFCCCCCC),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 点赞 + 回复
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Like",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                if (comment.likes > 0) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${comment.likes}",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Reply",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

/**
 * Row with top alignment for comments
 */
@Composable
private fun Row(
    modifier: Modifier = Modifier,
    crossAxisAlignment: Alignment.Vertical,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = crossAxisAlignment,
        content = content
    )
}
