package com.example.myspotify.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.data.AssetMapper
import com.example.myspotify.model.Album
import com.example.myspotify.model.Artist
import com.example.myspotify.model.Playlist
import com.example.myspotify.model.Song
import com.example.myspotify.ui.common.AssetImage

/**
 * 分类详情卡片数据
 */
data class CategoryContentItem(
    val title: String,
    val subtitle: String = "",
    val assetPath: String
)

/**
 * CategoryDetailTab View - 分类详情页面（StartBrowsing/BrowseAll 共用）
 */
@Composable
fun CategoryDetailTabView(
    categoryTitle: String,
    items: List<CategoryContentItem>,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 顶部栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
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
                text = categoryTitle,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 内容网格
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            val pairs = items.chunked(2)
            items(pairs.size) { index ->
                val pair = pairs[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { item ->
                        ContentCard(
                            item = item,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // 底部间距
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

/**
 * 内容卡片：封面图 + 标题 + 副标题
 */
@Composable
private fun ContentCard(
    item: CategoryContentItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { }
    ) {
        AssetImage(
            assetPath = item.assetPath,
            contentDescription = item.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2
        )
        if (item.subtitle.isNotEmpty()) {
            Text(
                text = item.subtitle,
                color = Color.Gray,
                fontSize = 12.sp,
                maxLines = 1
            )
        }
    }
}

/**
 * 根据分类名和来源生成推荐内容
 */
fun getCategoryItems(
    categoryTitle: String,
    source: String,
    albums: List<Album>,
    playlists: List<Playlist>,
    songs: List<Song>,
    artists: List<Artist>
): List<CategoryContentItem> {
    return when {
        source == "start_browsing" -> when (categoryTitle) {
            "Music" -> albums.map {
                CategoryContentItem(it.name, it.artistName, AssetMapper.albumCover(it.id))
            } + playlists.map {
                CategoryContentItem(it.name, it.creator, AssetMapper.playlistCover(it.id))
            }
            "Podcasts" -> listOf(
                CategoryContentItem("How AI is Changing Music", "The Daily", "cover/4.png"),
                CategoryContentItem("Behind the Lyrics", "Music Stories", "cover/5.png"),
                CategoryContentItem("Sound Design Weekly", "Audio Lab", "cover/6.png"),
                CategoryContentItem("Music History", "Culture Cast", "cover/7.png")
            )
            "Audiobooks" -> listOf(
                CategoryContentItem("The Art of Reading", "James Clear", "cover/8.png"),
                CategoryContentItem("Stories That Matter", "Sarah Johnson", "cover/9.png"),
                CategoryContentItem("Night Tales", "Fiction Hub", "cover/10.png"),
                CategoryContentItem("Science Explained", "Nova Press", "cover/11.png")
            )
            "Live Events" -> listOf(
                CategoryContentItem("Concert Live 2024", "Various Artists", "cover/12.png"),
                CategoryContentItem("Festival Season", "Live Nation", "cover/13.png"),
                CategoryContentItem("Acoustic Sessions", "Live Lounge", "cover/14.png"),
                CategoryContentItem("World Tour", "Global Stage", "cover/15.png")
            )
            else -> emptyList()
        }
        source == "browse_all" -> when (categoryTitle) {
            "Made For You" -> playlists.map {
                CategoryContentItem(it.name, it.creator, AssetMapper.playlistCover(it.id))
            }
            "New Releases" -> albums.map {
                CategoryContentItem(it.name, it.artistName, AssetMapper.albumCover(it.id))
            }
            "Pop" -> songs.filter {
                it.artistName == "Taylor Swift" || it.artistName == "Ed Sheeran"
            }.map {
                CategoryContentItem(it.title, it.artistName, AssetMapper.songCover(it))
            }
            "Hip Hop" -> songs.filter {
                it.artistName == "Drake"
            }.map {
                CategoryContentItem(it.title, it.artistName, AssetMapper.songCover(it))
            } + listOf(
                CategoryContentItem("Hip Hop Hits", "Spotify", "cover/4.png"),
                CategoryContentItem("Rap Caviar", "Spotify", "cover/6.png")
            )
            "R&B" -> songs.filter {
                it.artistName == "The Weeknd"
            }.map {
                CategoryContentItem(it.title, it.artistName, AssetMapper.songCover(it))
            }
            "K-Pop" -> listOf(
                CategoryContentItem("K-Pop Hits", "Spotify", "cover/8.png"),
                CategoryContentItem("K-Pop Rising", "Spotify", "cover/9.png"),
                CategoryContentItem("K-Pop Daebak", "Spotify", "cover/4.png"),
                CategoryContentItem("K-Pop ON!", "Spotify", "cover/6.png")
            )
            "Charts" -> playlists.take(3).map {
                CategoryContentItem(it.name, it.creator, AssetMapper.playlistCover(it.id))
            }
            else -> albums.map {
                CategoryContentItem(it.name, it.artistName, AssetMapper.albumCover(it.id))
            } + playlists.take(2).map {
                CategoryContentItem(it.name, it.creator, AssetMapper.playlistCover(it.id))
            }
        }
        else -> emptyList()
    }
}
