package com.example.myspotify.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.ui.common.AssetImage

private data class PodcastItem(val name: String, val description: String, val coverPath: String)

private val podcasts = listOf(
    PodcastItem("The Joe Rogan Experience", "Long form conversations with interesting people", "cover/8.png"),
    PodcastItem("Crime Junkie", "If you can never get enough true crime", "cover/9.png"),
    PodcastItem("The Daily", "The biggest stories of our time", "cover/10.png"),
    PodcastItem("Call Her Daddy", "The most talked about podcast in the world", "cover/11.png")
)

/**
 * AddPodcastsTab - 添加播客页面
 * 从 YourLibraryTab 新增入口进入
 */
@Composable
fun AddPodcastsTabView(
    onBack: () -> Unit,
    savedPodcastIds: MutableList<String> = mutableListOf()
) {
    var searchText by remember { mutableStateOf("") }

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
                text = "Add podcasts",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 搜索框
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = {
                Text("Search", color = Color.Gray, fontSize = 14.sp)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF282828),
                unfocusedContainerColor = Color(0xFF282828),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 标题
        Text(
            text = "Podcasts you might like",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 播客列表
        LazyColumn {
            items(podcasts) { podcast ->
                PodcastListItem(
                    podcast = podcast,
                    isFollowed = savedPodcastIds.contains(podcast.name),
                    onFollowToggle = {
                        if (savedPodcastIds.contains(podcast.name)) {
                            savedPodcastIds.remove(podcast.name)
                        } else {
                            savedPodcastIds.add(podcast.name)
                        }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

/**
 * 播客列表项（带 Follow 按钮）
 */
@Composable
private fun PodcastListItem(
    podcast: PodcastItem,
    isFollowed: Boolean = false,
    onFollowToggle: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 方形封面
        AssetImage(
            assetPath = podcast.coverPath,
            contentDescription = podcast.name,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 名称和描述
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = podcast.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = podcast.description,
                color = Color.Gray,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Follow 按钮
        OutlinedButton(
            onClick = onFollowToggle,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (isFollowed) Color(0xFF1DB954) else Color.Transparent,
                contentColor = if (isFollowed) Color.Black else Color.White
            ),
            border = if (isFollowed) null else ButtonDefaults.outlinedButtonBorder(enabled = true),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (isFollowed) "Following" else "Follow",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
