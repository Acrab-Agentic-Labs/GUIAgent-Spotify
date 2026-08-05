package com.example.myspotify.ui.common

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.model.Playlist

/**
 * SelectPlaylistView - 选择歌单页面
 * 从播放菜单的 "Add to playlist" 进入
 */
@Composable
fun SelectPlaylistView(
    playlists: List<Playlist>,
    likedSongsCount: Int,
    onBack: () -> Unit,
    onSelect: (Playlist) -> Unit,
    onSelectLikedSongs: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedId by remember { mutableStateOf<String?>(null) }
    var selectedIsLikedSongs by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val filteredPlaylists = remember(searchQuery, playlists) {
        if (searchQuery.isBlank()) playlists
        else playlists.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    val showLikedSongs = searchQuery.isBlank() || "liked songs".contains(searchQuery, ignoreCase = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF282828))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 顶部栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add to playlist",
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
                    .background(Color(0xFF3E3E3E))
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
                                text = "Find playlist",
                                color = Color.Gray,
                                fontSize = 15.sp
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = TextStyle(color = Color.White, fontSize = 15.sp),
                            cursorBrush = SolidColor(Color.White),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 歌单列表
            LazyColumn(modifier = Modifier.weight(1f)) {
                // Liked Songs
                if (showLikedSongs) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedIsLikedSongs = true
                                    selectedId = null
                                }
                                .background(
                                    if (selectedIsLikedSongs) Color(0xFF3E3E3E) else Color.Transparent
                                )
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xFF6B4FBB),
                                                Color(0xFF1DB954)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Liked",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Liked Songs",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "$likedSongsCount song${if (likedSongsCount != 1) "s" else ""}",
                                    color = Color.Gray,
                                    fontSize = 13.sp
                                )
                            }
                            if (selectedIsLikedSongs) {
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color(0xFF1DB954),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }

                // 用户创建的歌单
                items(filteredPlaylists) { playlist ->
                    val isSelected = selectedId == playlist.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedId = playlist.id
                                selectedIsLikedSongs = false
                            }
                            .background(
                                if (isSelected) Color(0xFF3E3E3E) else Color.Transparent
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF535353)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = playlist.name,
                                tint = Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = playlist.name,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${playlist.songIds.size} song${if (playlist.songIds.size != 1) "s" else ""}",
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        }
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color(0xFF1DB954),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // Done 按钮
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                val hasSelection = selectedId != null || selectedIsLikedSongs
                Button(
                    onClick = {
                        if (selectedIsLikedSongs) {
                            onSelectLikedSongs()
                        } else {
                            val playlist = playlists.find { it.id == selectedId }
                            if (playlist != null) onSelect(playlist)
                        }
                    },
                    enabled = hasSelection,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hasSelection) Color.White else Color(0xFF535353),
                        disabledContainerColor = Color(0xFF535353)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "Done",
                        color = if (hasSelection) Color.Black else Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
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
