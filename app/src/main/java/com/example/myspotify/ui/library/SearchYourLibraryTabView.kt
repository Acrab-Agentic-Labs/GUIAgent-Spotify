package com.example.myspotify.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
 * SearchYourLibraryTab - 搜索你的音乐库
 * 从 YourLibraryTab 右上角搜索图标进入
 */
@Composable
fun SearchYourLibraryTabView(
    followedArtists: List<Artist>,
    onCancel: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 搜索栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = {
                    Text(
                        text = "Search Your Library",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
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
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            TextButton(onClick = onCancel) {
                Text(
                    text = "Cancel",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        // 提示文字
        Text(
            text = "Find in your Liked Songs, playlists, and more.",
            color = Color.Gray,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Recent searches 标题
        Text(
            text = "Recent searches",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 最近搜索列表（显示已关注的艺术家）
        LazyColumn {
            items(followedArtists) { artist ->
                RecentSearchItem(artist = artist)
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

/**
 * 最近搜索项（艺术家）
 */
@Composable
private fun RecentSearchItem(artist: Artist) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AssetImage(
            assetPath = AssetMapper.artistAvatar(artist.id),
            contentDescription = artist.name,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = artist.name,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Artist",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}
