package com.example.myspotify.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.data.AssetMapper
import com.example.myspotify.model.Song
import com.example.myspotify.ui.common.AssetImage
import kotlinx.coroutines.launch

/**
 * LikedSongsTab - 喜欢的歌曲详情页
 * 从 YourLibraryTab 的 Liked Songs 项进入
 */
@Composable
fun LikedSongsTabView(
    likedSongs: List<Song>,
    onBack: () -> Unit,
    onSongClick: (Song) -> Unit = {},
    isInLibrary: Boolean = false,
    onToggleLibrary: (Boolean) -> Unit = {},
    onPlaylistMenuClick: () -> Unit = {},
    onSongMenuClick: (Song) -> Unit = {},
    likedSongIds: List<String> = emptyList(),
    onToggleLike: (Song) -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var addedToLibrary by remember(isInLibrary) { mutableStateOf(isInLibrary) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // 顶部栏（返回+菜单）
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onPlaylistMenuClick) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = Color.White
                        )
                    }
                }
            }

            // 渐变头图区域
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 渐变色方块 + 心形图标
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(8.dp))
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
                            modifier = Modifier.size(64.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 标题
                    Text(
                        text = "Liked Songs",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 歌曲数量
                    Text(
                        text = "${likedSongs.size} songs",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 操作按钮行（下载、分享）
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = "Download",
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        IconButton(
                            onClick = {
                                addedToLibrary = !addedToLibrary
                                onToggleLibrary(addedToLibrary)
                                coroutineScope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(
                                        if (addedToLibrary) "Added to Your Library"
                                        else "Removed from Your Library"
                                    )
                                }
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = if (addedToLibrary) Icons.Default.Check else Icons.Default.Add,
                                contentDescription = if (addedToLibrary) "Remove from Library" else "Add to Library",
                                tint = if (addedToLibrary) Color(0xFF1DB954) else Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Shuffle 按钮
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1DB954)
                            ),
                            shape = RoundedCornerShape(24.dp),
                            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shuffle,
                                contentDescription = "Shuffle",
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Shuffle",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Play 按钮
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1DB954))
                                .clickable {},
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // 歌曲列表
            items(likedSongs) { song ->
                LikedSongItem(song = song, onClick = { onSongClick(song) }, onMenuClick = { onSongMenuClick(song) }, isLiked = likedSongIds.contains(song.id), onLikeToggle = { onToggleLike(song) })
            }

            // Add to this playlist
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF282828)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Add to this playlist",
                        color = Color.Gray,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 底部间距
            item {
                Spacer(modifier = Modifier.height(100.dp))
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
}

/**
 * 喜欢的歌曲项
 */
@Composable
private fun LikedSongItem(song: Song, onClick: () -> Unit = {}, onMenuClick: () -> Unit = {}, isLiked: Boolean = true, onLikeToggle: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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

        IconButton(onClick = onLikeToggle) {
            Icon(
                imageVector = if (isLiked) Icons.Default.Check else Icons.Default.Add,
                contentDescription = if (isLiked) "Unlike" else "Like",
                tint = if (isLiked) Color(0xFF1DB954) else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }

        // 更多按钮
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
