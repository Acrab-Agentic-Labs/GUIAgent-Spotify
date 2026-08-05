package com.example.myspotify.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.example.myspotify.data.AssetMapper
import com.example.myspotify.model.Song
import com.example.myspotify.ui.common.AssetImage

/**
 * 最近搜索条目数据
 */
data class RecentSearchItem(
    val name: String,
    val type: String,
    val assetPath: String,
    val id: String
)

/**
 * SearchForSomethingTab View - 搜索输入页面
 */
@Composable
fun SearchForSomethingTabView(
    recentSearchItems: List<RecentSearchItem>,
    allSongs: List<Song> = emptyList(),
    onBack: () -> Unit,
    onSongClick: (Song) -> Unit = {},
    onPlaceholderClick: () -> Unit = {},
    onRecentSearchItemsChange: (List<RecentSearchItem>) -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }
    val displayItems = remember { mutableStateListOf<RecentSearchItem>().apply { addAll(recentSearchItems) } }

    LaunchedEffect(Unit) {
        onRecentSearchItemsChange(displayItems.toList())
    }

    // 搜索结果
    val searchResults = remember(searchText) {
        if (searchText.isBlank()) emptyList()
        else allSongs.filter { it.title.contains(searchText, ignoreCase = true) || it.artistName.contains(searchText, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 顶部搜索栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 搜索输入框
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text("What do you want to listen to?", color = Color.Gray, fontSize = 14.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = { searchText = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = Color.Gray
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF282828),
                    unfocusedContainerColor = Color(0xFF282828),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Cancel 按钮
            Text(
                text = "Cancel",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onBack() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (searchText.isNotBlank()) {
            // 搜索结果显示
            Text(
                text = "Search results",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (searchResults.isEmpty()) {
                Text(
                    text = "No results found for \"$searchText\"",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } else {
                LazyColumn {
                    items(searchResults) { song ->
                        SearchResultSongRow(
                            song = song,
                            onClick = { onSongClick(song) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        } else {
            // "Recent searches" 标题
            Text(
                text = "Recent searches",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 最近搜索列表
            LazyColumn {
                items(
                    items = displayItems,
                    key = { it.id }
                ) { item ->
                    RecentSearchRow(
                        item = item,
                        onClick = onPlaceholderClick,
                        onRemove = {
                            displayItems.remove(item)
                            onRecentSearchItemsChange(displayItems.toList())
                        }
                    )
                }

                // 底部间距
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

/**
 * 搜索结果歌曲行
 */
@Composable
private fun SearchResultSongRow(
    song: Song,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AssetImage(
            assetPath = AssetMapper.songCover(song),
            contentDescription = song.title,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Song · ${song.artistName}",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * 单条最近搜索记录
 */
@Composable
private fun RecentSearchRow(
    item: RecentSearchItem,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 圆形缩略图
        AssetImage(
            assetPath = item.assetPath,
            contentDescription = item.name,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 名称和类型
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = item.type,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        // X 删除按钮
        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
