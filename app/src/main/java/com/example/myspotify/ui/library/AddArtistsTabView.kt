package com.example.myspotify.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.data.AssetMapper
import com.example.myspotify.model.Artist
import com.example.myspotify.ui.common.AssetImage

/**
 * AddArtistsTab - 添加艺术家页面
 * 从 YourLibraryTab 的 "Add artists" 进入
 */
@Composable
fun AddArtistsTabView(
    allArtists: List<Artist>,
    followedArtists: List<Artist>,
    onBack: () -> Unit,
    followedArtistIds: MutableList<String> = mutableListOf(),
    onToggleFollow: (Artist) -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }

    // 显示所有非已关注的艺人 + 刚刚在本页面关注的艺人
    val initialNotFollowed = remember { allArtists.filter { it.id !in followedArtistIds } }

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
                text = "Add artists",
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
            text = "Artists you might like",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 推荐艺术家列表
        LazyColumn {
            items(initialNotFollowed) { artist ->
                SuggestedArtistItem(
                    artist = artist,
                    isFollowed = followedArtistIds.contains(artist.id),
                    onFollowToggle = { onToggleFollow(artist) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

/**
 * 推荐艺术家项（带 Follow 按钮）
 */
@Composable
private fun SuggestedArtistItem(
    artist: Artist,
    isFollowed: Boolean = false,
    onFollowToggle: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 头像
        AssetImage(
            assetPath = AssetMapper.artistAvatar(artist.id),
            contentDescription = artist.name,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 名称和类型
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = artist.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = artist.genres.firstOrNull() ?: "Artist",
                color = Color.Gray,
                fontSize = 14.sp
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
