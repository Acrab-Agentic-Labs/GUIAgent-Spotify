package com.example.myspotify.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
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
import com.example.myspotify.model.Album
import com.example.myspotify.model.Artist
import com.example.myspotify.model.Playlist
import com.example.myspotify.model.Song
import com.example.myspotify.ui.common.AssetImage

/**
 * SearchTab View - 搜索页面视图
 */
@Composable
fun SearchTabView(
    songs: List<Song>,
    artists: List<Artist>,
    albums: List<Album>,
    playlists: List<Playlist>,
    recentlyPlayedSongs: List<Song>,
    followedArtists: List<Artist>,
    onSongClick: (Song) -> Unit = {},
    onSearchStateChange: (List<Map<String, String>>, List<Map<String, String>>) -> Unit = { _, _ -> }
) {
    // 导航状态
    var showSearchForSomething by remember { mutableStateOf(false) }
    var showCategoryDetail by remember { mutableStateOf(false) }
    var selectedCategoryTitle by remember { mutableStateOf("") }
    var selectedCategorySource by remember { mutableStateOf("") }
    var showCodeTab by remember { mutableStateOf(false) }

    // 构建最近搜索数据
    val recentSearchItems = remember {
        val items = mutableListOf<RecentSearchItem>()
        recentlyPlayedSongs.forEach { song ->
            items.add(
                RecentSearchItem(
                    name = song.title,
                    type = "Song",
                    assetPath = AssetMapper.songCover(song),
                    id = song.id
                )
            )
        }
        followedArtists.forEach { artist ->
            items.add(
                RecentSearchItem(
                    name = artist.name,
                    type = "Artist",
                    assetPath = AssetMapper.artistAvatar(artist.id),
                    id = artist.id
                )
            )
        }
        items
    }

    fun toExportItems(items: List<RecentSearchItem>): List<Map<String, String>> = items.map { item ->
        mapOf(
            "id" to item.id,
            "name" to item.name,
            "type" to item.type
        )
    }

    // 子页面导航
    if (showSearchForSomething) {
        SearchForSomethingTabView(
            recentSearchItems = recentSearchItems,
            allSongs = songs,
            onBack = { showSearchForSomething = false },
            onSongClick = onSongClick,
            onRecentSearchItemsChange = { currentItems ->
                onSearchStateChange(toExportItems(recentSearchItems), toExportItems(currentItems))
            }
        )
        return
    }
    if (showCategoryDetail) {
        CategoryDetailTabView(
            categoryTitle = selectedCategoryTitle,
            items = getCategoryItems(
                selectedCategoryTitle, selectedCategorySource,
                albums, playlists, songs, artists
            ),
            onBack = { showCategoryDetail = false }
        )
        return
    }
    if (showCodeTab) {
        CodeTabView(onBack = { showCodeTab = false })
        return
    }

    // 分类数据
    val startBrowsingCategories = listOf(
        "Music" to Color(0xFFE91E63),
        "Podcasts" to Color(0xFF009688),
        "Audiobooks" to Color(0xFF3F51B5),
        "Live Events" to Color(0xFF9C27B0)
    )

    val browseAllCategories = listOf(
        "Made For You" to Color(0xFF1565C0),
        "New Releases" to Color(0xFFE65100),
        "Pop" to Color(0xFF1976D2),
        "Indie" to Color(0xFF4E342E),
        "K-Pop" to Color(0xFF7B1FA2),
        "Charts" to Color(0xFF2E7D32),
        "R&B" to Color(0xFF283593),
        "Hip Hop" to Color(0xFFD84315),
        "Rock" to Color(0xFF00838F),
        "Latin" to Color(0xFFFF6F00)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 顶部栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AssetImage(
                    assetPath = AssetMapper.userAvatar,
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Search",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(onClick = { showCodeTab = true }) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Camera",
                    tint = Color.White
                )
            }
        }

        // 搜索框（点击后进入搜索页面）
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .clickable { showSearchForSomething = true }
                .padding(horizontal = 12.dp, vertical = 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "What do you want to listen to?",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            // ===== 第一板块: Start browsing =====
            item {
                Text(
                    text = "Start browsing",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            val startPairs = startBrowsingCategories.chunked(2)
            items(startPairs.size) { index ->
                val pair = startPairs[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { (title, color) ->
                        CategoryCard(
                            title = title,
                            backgroundColor = color,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                selectedCategoryTitle = title
                                selectedCategorySource = "start_browsing"
                                showCategoryDetail = true
                            }
                        )
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // ===== 第三板块: Browse all =====
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Browse all",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            val browsePairs = browseAllCategories.chunked(2)
            items(browsePairs.size) { index ->
                val pair = browsePairs[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { (title, color) ->
                        CategoryCard(
                            title = title,
                            backgroundColor = color,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                selectedCategoryTitle = title
                                selectedCategorySource = "browse_all"
                                showCategoryDetail = true
                            }
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
 * 分类卡片组件
 */
@Composable
fun CategoryCard(
    title: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .height(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
    }
}

