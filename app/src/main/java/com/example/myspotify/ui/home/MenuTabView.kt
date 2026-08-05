package com.example.myspotify.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.data.AssetMapper
import com.example.myspotify.model.Song
import com.example.myspotify.ui.common.AssetImage

/**
 * MenuTab View - 播放页面菜单（点击右上角三点进入）
 */
@Composable
fun MenuTabView(
    song: Song? = null,
    onBack: () -> Unit,
    onAddToPlaylist: () -> Unit = {},
    onSleepTimer: () -> Unit = {},
    sleepTimerDuration: String? = null
) {
    val songTitle = song?.title ?: "IRIS OUT"
    val songArtist = song?.artistName ?: "Kenshi Yonezu"
    val songCover = if (song != null) AssetMapper.songCover(song) else "cover/13.png"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF282828))
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // 顶部歌曲信息
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 小专辑封面
            AssetImage(
                assetPath = songCover,
                contentDescription = "Album Cover",
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = songTitle,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = songArtist,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Divider(color = Color(0xFF535353), thickness = 0.5.dp)

        Spacer(modifier = Modifier.height(8.dp))

        // 菜单项列���
        MenuItem(icon = Icons.Default.FavoriteBorder, text = "Like")
        MenuItem(icon = Icons.Default.Block, text = "Hide this song")
        MenuItem(icon = Icons.Default.PlaylistAdd, text = "Add to playlist", onClick = onAddToPlaylist)
        MenuItem(icon = Icons.Default.QueueMusic, text = "Add to queue")
        MenuItem(icon = Icons.Default.Album, text = "View album")
        MenuItem(icon = Icons.Default.Person, text = "View artist")
        MenuItem(icon = Icons.Default.Share, text = "Share")
        MenuItem(icon = Icons.Default.Radio, text = "Song radio")
        MenuItem(icon = Icons.Default.Description, text = "Song credits")
        MenuItem(
            icon = Icons.Default.Timer,
            text = if (sleepTimerDuration != null) "Sleep timer · $sleepTimerDuration" else "Sleep timer",
            onClick = onSleepTimer,
            iconTint = if (sleepTimerDuration != null) Color(0xFF1DB954) else Color.White
        )

        Spacer(modifier = Modifier.weight(1f))

        // 关闭按钮
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

/**
 * 菜单项组件
 */
@Composable
private fun MenuItem(icon: ImageVector, text: String, onClick: () -> Unit = {}, iconTint: Color = Color.White) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}
