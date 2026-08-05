package com.example.myspotify.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.data.AssetMapper
import com.example.myspotify.model.Album
import com.example.myspotify.model.Artist
import com.example.myspotify.model.Playlist
import com.example.myspotify.model.Song
import com.example.myspotify.ui.common.AssetImage

/**
 * HomeTab View - 首页视图（支持子页面切换）
 */
@Composable
fun HomeTabView(
    songs: List<Song>,
    albums: List<Album>,
    playlists: List<Playlist>,
    artists: List<Artist>,
    recentlyPlayedSongs: List<Song>,
    onSongClick: (Song) -> Unit = {},
    onPlaylistClick: (Playlist) -> Unit = {},
    onAlbumClick: (Album) -> Unit = {},
    onSongMenuClick: (Song) -> Unit = {},
    likedSongIds: List<String> = emptyList(),
    onToggleLike: (Song) -> Unit = {},
    savedPodcastIds: MutableList<String> = mutableListOf(),
    savedAudiobookIds: MutableList<String> = mutableListOf()
) {
    var showUserTab by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Music", "Podcasts", "Audiobooks")
    var showPodcastDetail by remember { mutableStateOf(false) }
    var selectedPodcast by remember { mutableStateOf<PodcastFeedItem?>(null) }
    var showAudiobookDetail by remember { mutableStateOf(false) }
    var selectedAudiobook by remember { mutableStateOf<AudiobookFeedItem?>(null) }

    // 显示 UserTab
    if (showUserTab) {
        UserTabView(onBack = { showUserTab = false })
        return
    }

    // 显示播客详情
    if (showPodcastDetail && selectedPodcast != null) {
        PodcastDetailView(
            podcastItem = selectedPodcast!!,
            onBack = { showPodcastDetail = false; selectedPodcast = null },
            isSaved = savedPodcastIds.contains(selectedPodcast!!.title),
            onToggleSave = {
                if (savedPodcastIds.contains(selectedPodcast!!.title)) {
                    savedPodcastIds.remove(selectedPodcast!!.title)
                } else {
                    savedPodcastIds.add(selectedPodcast!!.title)
                }
            }
        )
        return
    }

    // 显示有声书详情
    if (showAudiobookDetail && selectedAudiobook != null) {
        AudiobookDetailView(
            audiobookItem = selectedAudiobook!!,
            onBack = { showAudiobookDetail = false; selectedAudiobook = null },
            isSaved = savedAudiobookIds.contains(selectedAudiobook!!.title),
            onToggleSave = {
                if (savedAudiobookIds.contains(selectedAudiobook!!.title)) {
                    savedAudiobookIds.remove(selectedAudiobook!!.title)
                } else {
                    savedAudiobookIds.add(selectedAudiobook!!.title)
                }
            }
        )
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 顶部用户头像和分类标签（所有子页面共用）
        item {
            Column {
                // 用户头像（可点击进入 UserTab）
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssetImage(
                        assetPath = AssetMapper.userAvatar,
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable { showUserTab = true }
                    )
                }

                // 分类标签
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1DB954),
                                selectedLabelColor = Color.Black,
                                containerColor = Color(0xFF282828),
                                labelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // 根据筛选标签显示不同内容
        when (selectedCategory) {
            "All" -> homeAllContent(songs, albums, playlists, recentlyPlayedSongs, onSongClick, onPlaylistClick, onAlbumClick, onSongMenuClick, likedSongIds, onToggleLike)
            "Music" -> homeMusicContent(songs, albums, playlists, recentlyPlayedSongs, onSongClick, onPlaylistClick, onAlbumClick, onSongMenuClick, likedSongIds, onToggleLike)
            "Podcasts" -> podcastsContent(onPodcastClick = { podcast ->
                selectedPodcast = podcast
                showPodcastDetail = true
            })
            "Audiobooks" -> audiobooksContent(onAudiobookClick = { audiobook ->
                selectedAudiobook = audiobook
                showAudiobookDetail = true
            })
        }
    }
}

/**
 * "All" 标签页内容
 */
fun LazyListScope.homeAllContent(
    songs: List<Song>,
    albums: List<Album>,
    playlists: List<Playlist>,
    recentlyPlayedSongs: List<Song>,
    onSongClick: (Song) -> Unit,
    onPlaylistClick: (Playlist) -> Unit,
    onAlbumClick: (Album) -> Unit,
    onSongMenuClick: (Song) -> Unit = {},
    likedSongIds: List<String> = emptyList(),
    onToggleLike: (Song) -> Unit = {}
) {
    // "Start listening" 部分
    item {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Jump into a session based on your tastes",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Start listening",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // 歌曲列表
    items(songs.take(3)) { song ->
        SongItem(song = song, onClick = { onSongClick(song) }, onMenuClick = { onSongMenuClick(song) }, isLiked = likedSongIds.contains(song.id), onLikeToggle = { onToggleLike(song) })
    }

    // "To get you started" 部分
    item {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "To get you started",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // 推荐卡片
    item {
        val mixPlaylists = albums.take(3).map { album ->
            Playlist(
                id = album.id,
                name = "${album.artistName} Mix",
                coverUrl = album.coverUrl,
                creator = "Spotify",
                description = "${album.artistName} and more",
                songIds = album.songIds
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(mixPlaylists) { playlist ->
                RecommendCard(
                    title = playlist.name,
                    subtitle = playlist.creator,
                    assetPath = AssetMapper.albumCover(playlist.id),
                    onClick = { onPlaylistClick(playlist) }
                )
            }
        }
    }

    // "Made For You" 板块
    item {
        val madeForYouPlaylists = listOf(
            Playlist("mfy_001", "Daily Mix 1", "", "Spotify", "Taylor Swift, Ed Sheeran", listOf("song_001", "song_002", "song_003", "song_004", "song_005", "song_006")),
            Playlist("mfy_002", "Daily Mix 2", "", "Spotify", "The Weeknd, Drake", listOf("song_007", "song_008", "song_009", "song_012")),
            Playlist("mfy_003", "Discover Weekly", "", "Spotify", "Your weekly mixtape", listOf("song_001", "song_004", "song_007", "song_010", "song_012")),
            Playlist("mfy_004", "Release Radar", "", "Spotify", "New music for you", listOf("song_002", "song_005", "song_008", "song_011")),
            Playlist("mfy_005", "Time Capsule", "", "Spotify", "Songs you loved", listOf("song_003", "song_006", "song_009", "song_010", "song_011"))
        )
        val madeForYouCovers = listOf("cover/8.png", "cover/9.png", "cover/10.png", "cover/11.png", "cover/12.png")

        HomeSection(
            title = "Made For You",
            items = madeForYouPlaylists.mapIndexed { index, playlist ->
                HomeSectionItem(
                    title = playlist.name,
                    subtitle = playlist.description,
                    assetPath = madeForYouCovers[index],
                    onClick = { onPlaylistClick(playlist) }
                )
            }
        )
    }

    // "Recently played" 板块
    item {
        HomeSection(
            title = "Recently played",
            items = recentlyPlayedSongs.map { song ->
                HomeSectionItem(
                    title = song.albumName,
                    subtitle = song.artistName,
                    assetPath = AssetMapper.songCover(song),
                    onClick = { onSongClick(song) }
                )
            } + playlists.take(2).map { playlist ->
                HomeSectionItem(
                    title = playlist.name,
                    subtitle = playlist.creator,
                    assetPath = AssetMapper.playlistCover(playlist.id),
                    onClick = { onPlaylistClick(playlist) }
                )
            }
        )
    }

    // "Popular playlists" 板块
    item {
        HomeSection(
            title = "Popular playlists",
            items = playlists.map { playlist ->
                HomeSectionItem(
                    title = playlist.name,
                    subtitle = playlist.description,
                    assetPath = AssetMapper.playlistCover(playlist.id),
                    onClick = { onPlaylistClick(playlist) }
                )
            }
        )
    }

    // 底部间距
    item {
        Spacer(modifier = Modifier.height(100.dp))
    }
}

/**
 * "Music" 标签页内容（与 All 相同但板块顺序调换）
 */
fun LazyListScope.homeMusicContent(
    songs: List<Song>,
    albums: List<Album>,
    playlists: List<Playlist>,
    recentlyPlayedSongs: List<Song>,
    onSongClick: (Song) -> Unit,
    onPlaylistClick: (Playlist) -> Unit,
    onAlbumClick: (Album) -> Unit,
    onSongMenuClick: (Song) -> Unit = {},
    likedSongIds: List<String> = emptyList(),
    onToggleLike: (Song) -> Unit = {}
) {
    // "Made For You" 板块（优先展示）
    item {
        val madeForYouPlaylists = listOf(
            Playlist("mfy_001", "Daily Mix 1", "", "Spotify", "Taylor Swift, Ed Sheeran", listOf("song_001", "song_002", "song_003", "song_004", "song_005", "song_006")),
            Playlist("mfy_002", "Daily Mix 2", "", "Spotify", "The Weeknd, Drake", listOf("song_007", "song_008", "song_009", "song_012")),
            Playlist("mfy_003", "Discover Weekly", "", "Spotify", "Your weekly mixtape", listOf("song_001", "song_004", "song_007", "song_010", "song_012")),
            Playlist("mfy_004", "Release Radar", "", "Spotify", "New music for you", listOf("song_002", "song_005", "song_008", "song_011")),
            Playlist("mfy_005", "Time Capsule", "", "Spotify", "Songs you loved", listOf("song_003", "song_006", "song_009", "song_010", "song_011"))
        )
        val madeForYouCovers = listOf("cover/8.png", "cover/9.png", "cover/10.png", "cover/11.png", "cover/12.png")

        HomeSection(
            title = "Made For You",
            items = madeForYouPlaylists.mapIndexed { index, playlist ->
                HomeSectionItem(
                    title = playlist.name,
                    subtitle = playlist.description,
                    assetPath = madeForYouCovers[index],
                    onClick = { onPlaylistClick(playlist) }
                )
            }
        )
    }

    // "Recently played" 板块
    item {
        HomeSection(
            title = "Recently played",
            items = recentlyPlayedSongs.map { song ->
                HomeSectionItem(
                    title = song.albumName,
                    subtitle = song.artistName,
                    assetPath = AssetMapper.songCover(song),
                    onClick = { onSongClick(song) }
                )
            } + playlists.take(2).map { playlist ->
                HomeSectionItem(
                    title = playlist.name,
                    subtitle = playlist.creator,
                    assetPath = AssetMapper.playlistCover(playlist.id),
                    onClick = { onPlaylistClick(playlist) }
                )
            }
        )
    }

    // "Start listening" 部分
    item {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            Text(
                text = "Jump into a session based on your tastes",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Start listening",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // 歌曲列表
    items(songs.take(3)) { song ->
        SongItem(song = song, onClick = { onSongClick(song) }, onMenuClick = { onSongMenuClick(song) }, isLiked = likedSongIds.contains(song.id), onLikeToggle = { onToggleLike(song) })
    }

    // "Popular playlists" 板块
    item {
        HomeSection(
            title = "Popular playlists",
            items = playlists.map { playlist ->
                HomeSectionItem(
                    title = playlist.name,
                    subtitle = playlist.description,
                    assetPath = AssetMapper.playlistCover(playlist.id),
                    onClick = { onPlaylistClick(playlist) }
                )
            }
        )
    }

    // "To get you started" 部分
    item {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "To get you started",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // 推荐卡片
    item {
        val mixPlaylists = albums.take(3).map { album ->
            Playlist(
                id = album.id,
                name = "${album.artistName} Mix",
                coverUrl = album.coverUrl,
                creator = "Spotify",
                description = "${album.artistName} and more",
                songIds = album.songIds
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(mixPlaylists) { playlist ->
                RecommendCard(
                    title = playlist.name,
                    subtitle = playlist.creator,
                    assetPath = AssetMapper.albumCover(playlist.id),
                    onClick = { onPlaylistClick(playlist) }
                )
            }
        }
    }

    // 底部间距
    item {
        Spacer(modifier = Modifier.height(100.dp))
    }
}

/**
 * 歌曲项组件
 */
@Composable
fun SongItem(song: Song, onClick: () -> Unit, onMenuClick: () -> Unit = {}, isLiked: Boolean = false, onLikeToggle: () -> Unit = {}) {
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
                .size(56.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = song.artistName,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        IconButton(onClick = onLikeToggle) {
            Icon(
                imageVector = if (isLiked) Icons.Default.Check else Icons.Default.Add,
                contentDescription = if (isLiked) "Unlike" else "Like",
                tint = if (isLiked) Color(0xFF1DB954) else Color.Gray
            )
        }

        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                tint = Color.Gray
            )
        }
    }
}

/**
 * 推荐卡片组件
 */
@Composable
fun RecommendCard(title: String, subtitle: String, assetPath: String, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick)
    ) {
        AssetImage(
            assetPath = assetPath,
            contentDescription = title,
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = subtitle,
            color = Color.Gray,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * 首页板块数据项
 */
data class HomeSectionItem(
    val title: String,
    val subtitle: String,
    val assetPath: String,
    val onClick: () -> Unit = {}
)

/**
 * 通用首页板块组件（标题 + 横向滚动卡片）
 */
@Composable
fun HomeSection(title: String, items: List<HomeSectionItem>) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(items) { item ->
                RecommendCard(
                    title = item.title,
                    subtitle = item.subtitle,
                    assetPath = item.assetPath,
                    onClick = item.onClick
                )
            }
        }
    }
}
