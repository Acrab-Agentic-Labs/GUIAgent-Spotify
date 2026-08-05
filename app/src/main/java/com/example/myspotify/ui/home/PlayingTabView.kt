package com.example.myspotify.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.data.AssetMapper
import com.example.myspotify.model.Playlist
import com.example.myspotify.model.Song
import com.example.myspotify.ui.common.AssetImage
import com.example.myspotify.ui.common.SelectPlaylistView
import kotlinx.coroutines.launch
import kotlin.math.abs

// Repeat mode: OFF -> ALL -> ONE
enum class RepeatMode { OFF, ALL, ONE }

/**
 * PlayingTab - 全屏播放页面（点击底部播放栏进入）
 */
@Composable
fun PlayingTabView(
    song: Song? = null,
    onBack: () -> Unit,
    isLiked: Boolean = false,
    onLikeToggle: () -> Unit = {},
    userPlaylists: List<Playlist> = emptyList(),
    likedSongsCount: Int = 0,
    onAddSongToPlaylist: (Song, Playlist) -> Unit = { _, _ -> },
    onAddSongToLikedSongs: (Song) -> Unit = {},
    onPrevious: () -> Unit = {},
    onNext: () -> Unit = {},
    isPlaying: Boolean = true,
    onPlayPauseToggle: () -> Unit = {},
    isShuffle: Boolean = false,
    onShuffleToggle: () -> Unit = {},
    repeatMode: RepeatMode = RepeatMode.OFF,
    onRepeatCycle: () -> Unit = {},
    sleepTimerDuration: String? = null,
    onSleepTimerChange: (String?) -> Unit = {}
) {
    val songTitle = song?.title ?: "IRIS OUT"
    val songArtist = song?.artistName ?: "Kenshi Yonezu"
    val songCover = if (song != null) AssetMapper.songCover(song) else "cover/13.png"
    val songDuration = song?.durationMs ?: 219000L
    var showMenuTab by remember { mutableStateOf(false) }
    var showLyricsTab by remember { mutableStateOf(false) }
    var showCreditsTab by remember { mutableStateOf(false) }
    var showAboutArtistTab by remember { mutableStateOf(false) }
    var showSelectPlaylist by remember { mutableStateOf(false) }
    var showSleepTimer by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // 睡眠定时器页面
    if (showSleepTimer) {
        SleepTimerView(
            isTimerActive = sleepTimerDuration != null,
            onBack = { showSleepTimer = false },
            onSelectDuration = { duration ->
                onSleepTimerChange(duration)
                showSleepTimer = false
                coroutineScope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar("Your sleep timer is set.")
                }
            },
            onTurnOff = {
                onSleepTimerChange(null)
                showSleepTimer = false
                coroutineScope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar("Your sleep timer is turned off.")
                }
            }
        )
        return
    }

    if (showSelectPlaylist) {
        SelectPlaylistView(
            playlists = userPlaylists,
            likedSongsCount = likedSongsCount,
            onBack = { showSelectPlaylist = false },
            onSelect = { playlist ->
                if (song != null) {
                    onAddSongToPlaylist(song, playlist)
                }
                showSelectPlaylist = false
                showMenuTab = false
                coroutineScope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar("Added to ${playlist.name}")
                }
            },
            onSelectLikedSongs = {
                if (song != null) {
                    onAddSongToLikedSongs(song)
                }
                showSelectPlaylist = false
                showMenuTab = false
                coroutineScope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar("Added to Liked Songs")
                }
            }
        )
        return
    }

    if (showMenuTab) {
        MenuTabView(
            song = song,
            onBack = { showMenuTab = false },
            onAddToPlaylist = { showSelectPlaylist = true },
            onSleepTimer = { showSleepTimer = true },
            sleepTimerDuration = sleepTimerDuration
        )
        return
    }
    if (showLyricsTab) {
        LyricsTabView(song = song, onBack = { showLyricsTab = false })
        return
    }
    if (showCreditsTab) {
        CreditsTabView(song = song, onBack = { showCreditsTab = false })
        return
    }
    if (showAboutArtistTab) {
        AboutTheArtistTabView(song = song, onBack = { showAboutArtistTab = false })
        return
    }

    var progress by remember { mutableStateOf(0.35f) }

    val totalMs = songDuration
    val currentMs = (totalMs * progress).toLong()

    Box(modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        // 顶部栏：返回 + 更多
        TopBar(onBack = onBack, onMenuClick = { showMenuTab = true })

        Spacer(modifier = Modifier.height(24.dp))

        // 大尺寸专辑封面
        AssetImage(
            assetPath = songCover,
            contentDescription = "Album Cover",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 歌曲信息 + 收藏按钮
        SongInfoRow(title = songTitle, artist = songArtist, isLiked = isLiked, onLikeToggle = onLikeToggle)

        Spacer(modifier = Modifier.height(16.dp))

        // 进度条
        ProgressSection(
            progress = progress,
            onProgressChange = { progress = it },
            currentMs = currentMs,
            totalMs = totalMs
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 播放控制按钮
        PlaybackControls(
            isPlaying = isPlaying,
            onPlayPauseToggle = onPlayPauseToggle,
            isShuffle = isShuffle,
            onShuffleToggle = onShuffleToggle,
            repeatMode = repeatMode,
            onRepeatCycle = onRepeatCycle,
            onPrevious = onPrevious,
            onNext = onNext
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 底部功能按钮
        BottomActionRow()

        Spacer(modifier = Modifier.height(32.dp))

        // Lyrics preview card
        LyricsPreviewCard(onShowMore = { showLyricsTab = true })

        Spacer(modifier = Modifier.height(16.dp))

        // About the artist card
        AboutArtistCard(song = song, onShowMore = { showAboutArtistTab = true })

        Spacer(modifier = Modifier.height(16.dp))

        // Credits card
        CreditsCard(song = song, onShowAllCredits = { showCreditsTab = true })

        Spacer(modifier = Modifier.height(32.dp))
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

@Composable
private fun TopBar(onBack: () -> Unit, onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
        Text(
            text = "Now Playing",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun SongInfoRow(title: String, artist: String, isLiked: Boolean, onLikeToggle: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = artist,
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
        IconButton(onClick = onLikeToggle) {
            Icon(
                imageVector = if (isLiked) Icons.Default.Check else Icons.Default.Add,
                contentDescription = if (isLiked) "Unlike" else "Like",
                tint = if (isLiked) Color(0xFF1DB954) else Color.White
            )
        }
    }
}

@Composable
private fun ProgressSection(
    progress: Float,
    onProgressChange: (Float) -> Unit,
    currentMs: Long,
    totalMs: Long
) {
    Slider(
        value = progress,
        onValueChange = onProgressChange,
        modifier = Modifier.fillMaxWidth(),
        colors = SliderDefaults.colors(
            thumbColor = Color.White,
            activeTrackColor = Color(0xFF1DB954),
            inactiveTrackColor = Color(0xFF535353)
        )
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = formatTime(currentMs), color = Color.Gray, fontSize = 12.sp)
        Text(text = formatTime(totalMs), color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
private fun PlaybackControls(
    isPlaying: Boolean,
    onPlayPauseToggle: () -> Unit,
    isShuffle: Boolean,
    onShuffleToggle: () -> Unit,
    repeatMode: RepeatMode,
    onRepeatCycle: () -> Unit,
    onPrevious: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Shuffle (最左边)
        IconButton(onClick = onShuffleToggle) {
            Icon(
                imageVector = Icons.Default.Shuffle,
                contentDescription = "Shuffle",
                tint = if (isShuffle) Color(0xFF1DB954) else Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        // 上一首
        IconButton(onClick = onPrevious) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = "Previous",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        // 播放/暂停（大圆按钮）
        IconButton(
            onClick = onPlayPauseToggle,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.White)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint = Color.Black,
                modifier = Modifier.size(36.dp)
            )
        }

        // 下一首
        IconButton(onClick = onNext) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "Next",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        // Repeat (最右边) - 三种状态: OFF -> ALL -> ONE
        IconButton(onClick = onRepeatCycle) {
            when (repeatMode) {
                RepeatMode.OFF -> Icon(
                    imageVector = Icons.Default.Repeat,
                    contentDescription = "Repeat Off",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                RepeatMode.ALL -> Icon(
                    imageVector = Icons.Default.Repeat,
                    contentDescription = "Repeat All",
                    tint = Color(0xFF1DB954),
                    modifier = Modifier.size(28.dp)
                )
                RepeatMode.ONE -> Icon(
                    imageVector = Icons.Default.RepeatOne,
                    contentDescription = "Repeat One",
                    tint = Color(0xFF1DB954),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun BottomActionRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 队列列表按钮（三横杠菜单样式）
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Queue",
                tint = Color.White
            )
        }
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = Color.White
            )
        }
    }
}

/**
 * Lyrics 预览卡片
 */
@Composable
private fun LyricsPreviewCard(onShowMore: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF3D5A80), Color(0xFF2A2A2A))
                    )
                )
                .padding(16.dp)
        ) {
            Text(
                text = "Lyrics",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "I close my eyes and see",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "A world that's meant for you and me",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "The colors bloom beneath the light",
                color = Color.White.copy(alpha = 0.4f),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Show more",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onShowMore() }
            )
        }
    }
}

/**
 * About the artist 卡��
 */
@Composable
private fun AboutArtistCard(song: Song? = null, onShowMore: () -> Unit) {
    val artistName = song?.artistName ?: "Kenshi Yonezu"
    val artistId = song?.artistId ?: "artist_005"
    val artistAvatar = AssetMapper.artistAvatar(artistId)
    val monthlyListeners = remember(artistId) {
        val hash = abs(artistId.hashCode())
        val base = (hash % 50 + 10) / 10.0
        "${base}M"
    }
    val artistBio = remember(artistName) {
        when (artistName) {
            "Taylor Swift" -> "Taylor Swift is an American singer-songwriter who has become one of the most influential music artists of the 21st century. Known for her storytelling ability and genre-spanning music."
            "Ed Sheeran" -> "Ed Sheeran is an English singer-songwriter and musician known for his heartfelt lyrics and acoustic-driven sound. His breakthrough came with his debut album \"+\" in 2011."
            "The Weeknd" -> "The Weeknd is a Canadian singer, songwriter, and record producer known for his distinctive vocal style and dark R&B sound."
            "Drake" -> "Drake is a Canadian rapper, singer, songwriter, and actor who has become one of the most commercially successful artists in music history."
            else -> "Kenshi Yonezu is a Japanese musician, singer-songwriter, and producer. Known for his unique blend of pop and rock, he has captivated audiences worldwide with his distinctive musical style and creative vision."
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 艺术家大图
            AssetImage(
                assetPath = artistAvatar,
                contentDescription = "Artist",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "About the artist",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = artistName,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$monthlyListeners monthly listeners",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = artistBio,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Show more",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onShowMore() }
                )
            }
        }
    }
}

/**
 * Credits 卡片
 */
@Composable
private fun CreditsCard(song: Song? = null, onShowAllCredits: () -> Unit) {
    val artistName = song?.artistName ?: "Kenshi Yonezu"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Credits",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            CreditItem(role = "Performed by", name = artistName)
            Spacer(modifier = Modifier.height(12.dp))
            CreditItem(role = "Written by", name = artistName)
            Spacer(modifier = Modifier.height(12.dp))
            CreditItem(role = "Produced by", name = artistName)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Show all credits",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onShowAllCredits() }
            )
        }
    }
}

@Composable
private fun CreditItem(role: String, name: String) {
    Column {
        Text(text = name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Text(text = role, color = Color.Gray, fontSize = 12.sp)
    }
}

/**
 * 格式化毫秒为 m:ss
 */
private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "$minutes:${seconds.toString().padStart(2, '0')}"
}
