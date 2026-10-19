package com.example.myspotify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.myspotify.data.AssetMapper
import com.example.myspotify.data.AutoTestExporter
import com.example.myspotify.data.DataManager
import com.example.myspotify.model.Album
import com.example.myspotify.model.Artist
import com.example.myspotify.model.Playlist
import com.example.myspotify.model.Song
import com.example.myspotify.ui.common.AssetImage
import com.example.myspotify.ui.common.DevelopmentPlaceholderView
import com.example.myspotify.ui.common.MusicMenuView
import com.example.myspotify.ui.common.PlaylistMenuView
import com.example.myspotify.ui.home.PlayingTabView
import com.example.myspotify.ui.home.RepeatMode
import com.example.myspotify.ui.home.HomeTabView
import com.example.myspotify.ui.library.AddToPlaylistTabView
import com.example.myspotify.ui.library.PlaylistDetailTabView
import com.example.myspotify.ui.library.YourLibraryTabView
import com.example.myspotify.ui.premium.PremiumTabView
import com.example.myspotify.ui.search.SearchTabView
import com.example.myspotify.ui.theme.MySpotifyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MySpotifyTheme {
                MainScreen(
                    dataManager = DataManager(this),
                    autoTestExporter = AutoTestExporter(this)
                )
            }
        }
    }
}

@Composable
fun MainScreen(dataManager: DataManager, autoTestExporter: AutoTestExporter) {
    var selectedTab by remember { mutableStateOf(0) }
    var showPlayingTab by remember { mutableStateOf(false) }
    var showPlaylistDetail by remember { mutableStateOf(false) }
    var selectedPlaylist by remember { mutableStateOf<Playlist?>(null) }
    var showAlbumDetail by remember { mutableStateOf(false) }
    var selectedAlbum by remember { mutableStateOf<Album?>(null) }
    var showPlaylistMenu by remember { mutableStateOf(false) }
    var menuPlaylist by remember { mutableStateOf<Playlist?>(null) }
    var showMusicMenu by remember { mutableStateOf(false) }
    var menuSong by remember { mutableStateOf<Song?>(null) }
    var showAddToPlaylistTab by remember { mutableStateOf(false) }
    var addToPlaylistTarget by remember { mutableStateOf<Playlist?>(null) }
    var showPlaceholderTab by remember { mutableStateOf(false) }
    val userPlaylists = remember { mutableStateListOf<Playlist>() }
    val savedPlaylistIds = remember {
        mutableStateListOf<String>().apply { addAll(dataManager.getUserData().savedPlaylists) }
    }
    // 存储动态创建的歌单（如 Made For You、To get you started），以便在音乐库中查找
    val dynamicPlaylists = remember { mutableStateMapOf<String, Playlist>() }
    val savedAlbumIds = remember { mutableStateListOf<String>() }
    val savedPodcastIds = remember { mutableStateListOf<String>() }
    val savedAudiobookIds = remember { mutableStateListOf<String>() }

    val songs = remember { dataManager.getSongs() }
    val defaultSong = remember { dataManager.getSongById("song_013") }
    var selectedSong by remember { mutableStateOf(defaultSong) }
    var isPlaying by remember { mutableStateOf(true) }
    var isShuffle by remember { mutableStateOf(false) }
    var repeatMode by remember { mutableStateOf(RepeatMode.OFF) }
    var isPremium by remember { mutableStateOf(false) }
    // 睡眠定时器初始为开启状态：SleepTimerView 只有在定时器激活时才显示 "Turn off timer"，
    // 若初始为关闭，"关闭定时器"这个任务既无法执行，也无法与"从未设置过定时器"的状态区分。
    var sleepTimerDuration by remember { mutableStateOf<String?>("30 minutes") }
    val artists = remember { dataManager.getArtists() }
    val albums = remember { dataManager.getAlbums() }
    val playlists = remember { dataManager.getPlaylists() }
    val recentlyPlayedSongs = remember {
        dataManager.getUserData().recentlyPlayed.mapNotNull { dataManager.getSongById(it) }
    }
    val followedArtistIds = remember {
        mutableStateListOf<String>().apply { addAll(dataManager.getUserData().followedArtists) }
    }
    val followedArtists by remember {
        derivedStateOf { followedArtistIds.mapNotNull { dataManager.getArtistById(it) } }
    }
    val savedPlaylists by remember {
        derivedStateOf {
            savedPlaylistIds.mapNotNull { id ->
                dataManager.getPlaylistById(id) ?: dynamicPlaylists[id]
            }
        }
    }
    val savedAlbums by remember {
        derivedStateOf { savedAlbumIds.mapNotNull { id -> albums.find { it.id == id } } }
    }

    // 关注/取消关注艺术家
    val onToggleFollow: (Artist) -> Unit = { artist ->
        if (followedArtistIds.contains(artist.id)) {
            followedArtistIds.remove(artist.id)
        } else {
            followedArtistIds.add(artist.id)
        }
    }
    val likedSongIds = remember {
        mutableStateListOf<String>().apply { addAll(dataManager.getUserData().likedSongs) }
    }
    val likedSongs by remember {
        derivedStateOf { likedSongIds.mapNotNull { dataManager.getSongById(it) } }
    }

    // AutoTest: 导出状态的辅助函数
    val exportState = {
        val playMode = when {
            isShuffle -> "SHUFFLE"
            repeatMode == RepeatMode.ONE -> "REPEAT_ONE"
            repeatMode == RepeatMode.ALL -> "REPEAT_ALL"
            else -> "SEQUENTIAL"
        }
        autoTestExporter.exportUserState(
            likedSongIds = likedSongIds.toList(),
            followedArtistIds = followedArtistIds.toList(),
            savedPodcastIds = savedPodcastIds.toList(),
            savedAudiobookIds = savedAudiobookIds.toList(),
            savedPlaylistIds = (userPlaylists.map { it.id } + savedPlaylistIds).distinct()
        )
        autoTestExporter.exportUserPlaylists(userPlaylists.toList())
        autoTestExporter.exportPlaybackState(
            currentSongId = selectedSong?.id,
            isPlaying = isPlaying,
            playMode = playMode,
            shuffleEnabled = isShuffle,
            sleepTimerDuration = sleepTimerDuration
        )
        autoTestExporter.exportPremiumState(
            isPremium = isPremium,
            subscriptionDate = if (isPremium) java.time.LocalDateTime.now().toString() else null
        )
    }

    // AutoTest: 状态变化时自动导出
    // 使用 hashCode 作为 key 来检测 userPlaylists 内容变化（包括 songIds 变更）
    val playlistsHash = userPlaylists.sumOf { it.songIds.size * 31 + it.hashCode() }
    LaunchedEffect(likedSongIds.size, followedArtistIds.size, playlistsHash,
        savedPodcastIds.size, savedAudiobookIds.size, savedPlaylistIds.size, selectedSong?.id,
        isPlaying, isShuffle, repeatMode, isPremium, sleepTimerDuration) {
        exportState()
    }

    // 收藏/取消收藏歌曲
    val onToggleLike: (Song) -> Unit = { song ->
        if (likedSongIds.contains(song.id)) {
            likedSongIds.remove(song.id)
        } else {
            likedSongIds.add(song.id)
        }
    }

    // 通用的歌曲点击处理
    val onSongClick: (Song) -> Unit = { song ->
        selectedSong = song
        showPlayingTab = true
    }

    // 歌单点击处理
    val onPlaylistClick: (Playlist) -> Unit = { playlist ->
        selectedPlaylist = playlist
        showPlaylistDetail = true
        // 注册动态歌单，以便收藏后能在音乐库中显示
        if (dataManager.getPlaylistById(playlist.id) == null) {
            dynamicPlaylists[playlist.id] = playlist
        }
    }

    // 专辑点击处理（复用歌单详情页的布局）
    val onAlbumClick: (Album) -> Unit = { album ->
        selectedAlbum = album
        showAlbumDetail = true
    }

    // 歌单菜单点击处理
    val onPlaylistMenuClick: (Playlist) -> Unit = { playlist ->
        menuPlaylist = playlist
        showPlaylistMenu = true
    }

    // 歌曲菜单点击处理
    val onSongMenuClick: (Song) -> Unit = { song ->
        menuSong = song
        showMusicMenu = true
    }
    val onPlaceholderClick: () -> Unit = {
        showPlaceholderTab = true
    }

    if (showPlaceholderTab) {
        DevelopmentPlaceholderView(
            onBack = { showPlaceholderTab = false }
        )
        return
    }

    // 显示添加歌曲到歌单页面
    if (showAddToPlaylistTab && addToPlaylistTarget != null) {
        val playlist = addToPlaylistTarget!!
        // 检查是否有修改过的版本
        val actualPlaylist = userPlaylists.find { it.id == playlist.id } ?: playlist
        AddToPlaylistTabView(
            playlistName = actualPlaylist.name,
            allSongs = songs,
            addedSongIds = actualPlaylist.songIds,
            onBack = { showAddToPlaylistTab = false; addToPlaylistTarget = null },
            onAddSong = { song ->
                // 如果是用户创建的歌单，直接更新
                val userIndex = userPlaylists.indexOfFirst { it.id == actualPlaylist.id }
                if (userIndex >= 0 && !actualPlaylist.songIds.contains(song.id)) {
                    val updated = actualPlaylist.copy(songIds = actualPlaylist.songIds + song.id)
                    userPlaylists[userIndex] = updated
                    addToPlaylistTarget = updated
                } else if (userIndex < 0) {
                    // 如果是收藏的歌单，创建一个可修改的副本
                    if (!actualPlaylist.songIds.contains(song.id)) {
                        val updated = actualPlaylist.copy(songIds = actualPlaylist.songIds + song.id)
                        userPlaylists.add(updated)
                        addToPlaylistTarget = updated
                    }
                }
            }
        )
        return
    }

    // 显示歌单菜单
    if (showPlaylistMenu && menuPlaylist != null) {
        PlaylistMenuView(
            playlist = menuPlaylist!!,
            onBack = { showPlaylistMenu = false; menuPlaylist = null },
            onPlaceholderClick = onPlaceholderClick
        )
        return
    }

    // 显示歌曲菜单
    if (showMusicMenu && menuSong != null) {
        MusicMenuView(
            song = menuSong!!,
            onBack = { showMusicMenu = false; menuSong = null },
            onPlaceholderClick = onPlaceholderClick
        )
        return
    }

    // 显示全屏播放页面
    if (showPlayingTab) {
        PlayingTabView(
            song = selectedSong,
            onBack = { showPlayingTab = false },
            isLiked = selectedSong?.let { likedSongIds.contains(it.id) } ?: false,
            onLikeToggle = { selectedSong?.let { onToggleLike(it) } },
            userPlaylists = userPlaylists,
            likedSongsCount = likedSongIds.size,
            onAddSongToPlaylist = { s, playlist ->
                val index = userPlaylists.indexOfFirst { it.id == playlist.id }
                if (index >= 0 && !playlist.songIds.contains(s.id)) {
                    userPlaylists[index] = playlist.copy(songIds = playlist.songIds + s.id)
                }
            },
            onAddSongToLikedSongs = { s ->
                if (!likedSongIds.contains(s.id)) {
                    likedSongIds.add(s.id)
                }
            },
            isPlaying = isPlaying,
            onPlayPauseToggle = { isPlaying = !isPlaying },
            isShuffle = isShuffle,
            onShuffleToggle = { isShuffle = !isShuffle },
            repeatMode = repeatMode,
            onRepeatCycle = {
                repeatMode = when (repeatMode) {
                    RepeatMode.OFF -> RepeatMode.ALL
                    RepeatMode.ALL -> RepeatMode.ONE
                    RepeatMode.ONE -> RepeatMode.OFF
                }
            },
            sleepTimerDuration = sleepTimerDuration,
            onSleepTimerChange = { duration -> sleepTimerDuration = duration },
            onPlaceholderClick = onPlaceholderClick,
            onPrevious = {
                val currentIndex = songs.indexOfFirst { it.id == selectedSong?.id }
                if (currentIndex > 0) {
                    selectedSong = songs[currentIndex - 1]
                } else if (songs.isNotEmpty()) {
                    selectedSong = songs.last()
                }
            },
            onNext = {
                val currentIndex = songs.indexOfFirst { it.id == selectedSong?.id }
                if (currentIndex >= 0 && currentIndex < songs.size - 1) {
                    selectedSong = songs[currentIndex + 1]
                } else if (songs.isNotEmpty()) {
                    selectedSong = songs.first()
                }
            }
        )
        return
    }

    // 显示歌单详情页
    if (showPlaylistDetail && selectedPlaylist != null) {
        val playlist = selectedPlaylist!!
        // 检查是否有修改过的版本
        val actualPlaylist = userPlaylists.find { it.id == playlist.id } ?: playlist
        val playlistSongs = remember(actualPlaylist.songIds) {
            actualPlaylist.songIds.mapNotNull { dataManager.getSongById(it) }
        }
        PlaylistDetailTabView(
            playlist = actualPlaylist,
            playlistSongs = playlistSongs,
            onBack = { showPlaylistDetail = false; selectedPlaylist = null },
            onSongClick = onSongClick,
            onPlaylistMenuClick = { onPlaylistMenuClick(actualPlaylist) },
            onSongMenuClick = onSongMenuClick,
            likedSongIds = likedSongIds,
            onToggleLike = onToggleLike,
            isInLibrary = savedPlaylistIds.contains(playlist.id),
            onToggleLibrary = { isAdded ->
                if (isAdded) {
                    if (!savedPlaylistIds.contains(playlist.id)) {
                        savedPlaylistIds.add(playlist.id)
                    }
                } else {
                    savedPlaylistIds.remove(playlist.id)
                }
            },
            onAddToPlaylist = {
                addToPlaylistTarget = actualPlaylist
                showAddToPlaylistTab = true
            },
            onPlaceholderClick = onPlaceholderClick
        )
        return
    }

    // 显示专辑详情页（复用歌单详情页）
    if (showAlbumDetail && selectedAlbum != null) {
        val album = selectedAlbum!!
        val albumPlaylist = Playlist(
            id = album.id,
            name = album.name,
            coverUrl = album.coverUrl,
            creator = album.artistName,
            description = "${album.releaseYear}",
            songIds = album.songIds
        )
        val albumSongs = remember(album.id) {
            album.songIds.mapNotNull { dataManager.getSongById(it) }
        }
        PlaylistDetailTabView(
            playlist = albumPlaylist,
            playlistSongs = albumSongs,
            onBack = { showAlbumDetail = false; selectedAlbum = null },
            onSongClick = onSongClick,
            onPlaylistMenuClick = { onPlaylistMenuClick(albumPlaylist) },
            onSongMenuClick = onSongMenuClick,
            likedSongIds = likedSongIds,
            onToggleLike = onToggleLike,
            isInLibrary = savedAlbumIds.contains(album.id),
            onToggleLibrary = { isAdded ->
                if (isAdded) {
                    if (!savedAlbumIds.contains(album.id)) {
                        savedAlbumIds.add(album.id)
                    }
                } else {
                    savedAlbumIds.remove(album.id)
                }
            },
            onPlaceholderClick = onPlaceholderClick
        )
        return
    }

    Scaffold(
        bottomBar = {
            Column {
                MiniPlayer(
                    song = selectedSong,
                    isLiked = selectedSong?.let { likedSongIds.contains(it.id) } ?: false,
                    onLikeToggle = { selectedSong?.let { onToggleLike(it) } },
                    onClick = { showPlayingTab = true },
                    onPlaceholderClick = onPlaceholderClick
                )
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> HomeTabView(
                    songs = songs,
                    albums = albums,
                    playlists = playlists,
                    artists = artists,
                    recentlyPlayedSongs = recentlyPlayedSongs,
                    onSongClick = onSongClick,
                    onPlaylistClick = onPlaylistClick,
                    onAlbumClick = onAlbumClick,
                    onSongMenuClick = onSongMenuClick,
                    likedSongIds = likedSongIds,
                    onToggleLike = onToggleLike,
                    savedPodcastIds = savedPodcastIds,
                    savedAudiobookIds = savedAudiobookIds,
                    onPlaceholderClick = onPlaceholderClick
                )
                1 -> SearchTabView(
                    songs = songs,
                    artists = artists,
                    albums = albums,
                    playlists = playlists,
                    recentlyPlayedSongs = recentlyPlayedSongs,
                    followedArtists = followedArtists,
                    onSongClick = onSongClick,
                    onPlaceholderClick = onPlaceholderClick,
                    onSearchStateChange = { initialItems, currentItems ->
                        autoTestExporter.exportSearchState(initialItems, currentItems)
                    }
                )
                2 -> YourLibraryTabView(
                    artists = artists,
                    songs = songs,
                    albums = albums,
                    likedSongs = likedSongs,
                    followedArtists = followedArtists,
                    onSongClick = onSongClick,
                    onSongMenuClick = onSongMenuClick,
                    likedSongIds = likedSongIds,
                    onToggleLike = onToggleLike,
                    userPlaylists = userPlaylists,
                    savedPlaylists = savedPlaylists,
                    savedAlbums = savedAlbums,
                    onAlbumClick = onAlbumClick,
                    followedArtistIds = followedArtistIds,
                    onToggleFollow = onToggleFollow,
                    savedPodcastIds = savedPodcastIds,
                    savedAudiobookIds = savedAudiobookIds,
                    onPlaceholderClick = onPlaceholderClick
                )
                3 -> PremiumTabView(
                    onPremiumActivated = { isPremium = true },
                    onPlaceholderClick = onPlaceholderClick
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 12.sp) },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search", fontSize = 12.sp) },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "Your Library") },
            label = { Text("Your Library", fontSize = 12.sp) },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Star, contentDescription = "Premium") },
            label = { Text("Premium", fontSize = 12.sp) },
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun MiniPlayer(
    song: Song? = null,
    isLiked: Boolean = false,
    onLikeToggle: () -> Unit = {},
    onClick: () -> Unit = {},
    onPlaceholderClick: () -> Unit = {}
) {
    val coverPath = if (song != null) AssetMapper.songCover(song) else "cover/13.png"
    val title = song?.title ?: "IRIS OUT"
    val artist = song?.artistName ?: "Kenshi Yonezu"

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        color = Color(0xFF8B0000),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AssetImage(
                assetPath = coverPath,
                contentDescription = "Now Playing Cover",
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = artist,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPlaceholderClick) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Devices",
                        tint = Color.White
                    )
                }
                IconButton(onClick = onLikeToggle) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = if (isLiked) "Unlike" else "Like",
                        tint = if (isLiked) Color(0xFF1DB954) else Color.White
                    )
                }
                IconButton(onClick = onPlaceholderClick) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
