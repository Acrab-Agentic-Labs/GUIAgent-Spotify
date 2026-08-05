package com.example.myspotify.ui.common

import androidx.compose.foundation.background
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

/**
 * Music_menu - 歌曲菜单页面
 * 点击歌单内歌曲右侧三点按钮弹出
 */
@Composable
fun MusicMenuView(
    song: Song,
    onBack: () -> Unit
) {
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
            AssetImage(
                assetPath = AssetMapper.songCover(song),
                contentDescription = song.title,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = song.artistName,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(color = Color(0xFF535353), thickness = 0.5.dp)

        Spacer(modifier = Modifier.height(8.dp))

        // 菜单项列表
        MusicMenuItem(icon = Icons.Default.FavoriteBorder, text = "Like")
        MusicMenuItem(icon = Icons.Default.Block, text = "Hide this song")
        MusicMenuItem(icon = Icons.Default.PlaylistAdd, text = "Add to playlist")
        MusicMenuItem(icon = Icons.Default.QueueMusic, text = "Add to queue")
        MusicMenuItem(icon = Icons.Default.Album, text = "View album")
        MusicMenuItem(icon = Icons.Default.Person, text = "View artist")
        MusicMenuItem(icon = Icons.Default.Share, text = "Share")
        MusicMenuItem(icon = Icons.Default.Description, text = "Song credits")
        MusicMenuItem(icon = Icons.Default.Timer, text = "Sleep timer")
        MusicMenuItem(icon = Icons.Default.Radio, text = "Go to radio")
        MusicMenuItem(icon = Icons.Default.Flag, text = "Report")

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
 * 歌曲菜单项组件
 */
@Composable
private fun MusicMenuItem(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White,
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
