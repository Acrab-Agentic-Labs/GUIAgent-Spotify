package com.example.myspotify.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.data.AssetMapper
import com.example.myspotify.model.Song
import com.example.myspotify.ui.common.AssetImage
import kotlinx.coroutines.launch

/**
 * AddToPlaylistTab - 添加歌曲到歌单的页面
 * 从空歌单页面点击 "Add to this playlist" 进入
 */
@Composable
fun AddToPlaylistTabView(
    playlistName: String,
    allSongs: List<Song>,
    addedSongIds: List<String>,
    onBack: () -> Unit,
    onAddSong: (Song) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // 根据搜索过滤歌曲
    val filteredSongs = remember(searchQuery, allSongs) {
        if (searchQuery.isBlank()) {
            allSongs
        } else {
            allSongs.filter { song ->
                song.title.contains(searchQuery, ignoreCase = true) ||
                song.artistName.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                    text = "Add to this playlist",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // 搜索栏
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF282828))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search for songs",
                                color = Color.Gray,
                                fontSize = 15.sp
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 15.sp
                            ),
                            cursorBrush = SolidColor(Color.White),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Suggested 标题
            Text(
                text = if (searchQuery.isBlank()) "Suggested" else "Results",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )

            // 歌曲列表
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(filteredSongs) { song ->
                    val isAdded = addedSongIds.contains(song.id)
                    AddSongItem(
                        song = song,
                        isAdded = isAdded,
                        onAdd = {
                            if (!isAdded) {
                                onAddSong(song)
                                coroutineScope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar("Added to $playlistName")
                                }
                            }
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) { data ->
            Snackbar(
                snackbarData = data,
                containerColor = Color.White,
                contentColor = Color.Black
            )
        }
    }
}

/**
 * 可添加的歌曲项
 */
@Composable
private fun AddSongItem(
    song: Song,
    isAdded: Boolean,
    onAdd: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 封面
        AssetImage(
            assetPath = AssetMapper.songCover(song),
            contentDescription = song.title,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 歌曲信息
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = song.artistName,
                color = Color.Gray,
                fontSize = 13.sp
            )
        }

        // 添加按钮
        IconButton(onClick = onAdd) {
            Icon(
                imageVector = if (isAdded) Icons.Default.Check else Icons.Default.Add,
                contentDescription = if (isAdded) "Added" else "Add",
                tint = if (isAdded) Color(0xFF1DB954) else Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
