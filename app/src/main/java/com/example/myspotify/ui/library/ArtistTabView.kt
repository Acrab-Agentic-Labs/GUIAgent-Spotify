package com.example.myspotify.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.myspotify.model.Album
import com.example.myspotify.model.Song
import com.example.myspotify.model.Artist
import com.example.myspotify.ui.common.AssetImage
import kotlin.math.abs

/**
 * ArtistTab - 艺术家详情页
 * 从 YourLibraryTab 点击已关注的艺术家进入
 */
@Composable
fun ArtistTabView(
    artist: Artist,
    artistSongs: List<Song>,
    artistAlbums: List<Album>,
    onBack: () -> Unit,
    onSongClick: (Song) -> Unit = {},
    onSongMenuClick: (Song) -> Unit = {},
    likedSongIds: List<String> = emptyList(),
    onToggleLike: (Song) -> Unit = {},
    isFollowed: Boolean = false,
    onToggleFollow: () -> Unit = {}
) {
    // 根据 artist id 生成一个稳定的 "monthly listeners" 数字
    val monthlyListeners = remember(artist.id) {
        val hash = abs(artist.id.hashCode())
        val base = (hash % 900 + 100) * 1000 + (hash % 1000)
        "%,d".format(base)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
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

        // 艺术家头像
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AssetImage(
                    assetPath = AssetMapper.artistAvatar(artist.id),
                    contentDescription = artist.name,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 艺术家名
                Text(
                    text = artist.name,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // monthly listeners
                Text(
                    text = "$monthlyListeners monthly listeners",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Follow / Following 按钮
                OutlinedButton(
                    onClick = onToggleFollow,
                    modifier = Modifier.height(36.dp),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isFollowed) Color(0xFF1DB954) else Color.White
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isFollowed) Color.Transparent else Color.Transparent
                    )
                ) {
                    Text(
                        text = if (isFollowed) "Following" else "Follow",
                        color = if (isFollowed) Color(0xFF1DB954) else Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Shuffle + Play 按钮行
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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

                    Spacer(modifier = Modifier.weight(1f))

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

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Popular 标题
        item {
            Text(
                text = "Popular",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // 歌曲列表（带序号）
        itemsIndexed(artistSongs) { index, song ->
            PopularSongItem(index = index + 1, song = song, onClick = { onSongClick(song) }, onMenuClick = { onSongMenuClick(song) }, isLiked = likedSongIds.contains(song.id), onLikeToggle = { onToggleLike(song) })
        }

        // Discography 标题
        if (artistAlbums.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Discography",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // 横向滚动专辑卡片
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(artistAlbums) { album ->
                        AlbumCard(album = album)
                    }
                }
            }
        }

        // 底部间距
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

/**
 * 热门歌曲项（带序号）
 */
@Composable
private fun PopularSongItem(index: Int, song: Song, onClick: () -> Unit = {}, onMenuClick: () -> Unit = {}, isLiked: Boolean = false, onLikeToggle: () -> Unit = {}) {
    // 根据 song id 生成稳定的播放量数字
    val plays = remember(song.id) {
        val hash = abs(song.id.hashCode())
        val base = (hash % 9000 + 1000) * 1000L + (hash % 1000)
        "%,d".format(base)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 序号
        Text(
            text = "$index",
            color = Color.Gray,
            fontSize = 16.sp,
            modifier = Modifier.width(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

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
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = plays,
                color = Color.Gray,
                fontSize = 13.sp
            )
        }

        // 收藏按钮
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

/**
 * 专辑卡片
 */
@Composable
private fun AlbumCard(album: Album) {
    Column(
        modifier = Modifier.width(140.dp)
    ) {
        AssetImage(
            assetPath = AssetMapper.albumCover(album.id),
            contentDescription = album.name,
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = album.name,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "${album.releaseYear}",
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}
