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
import com.example.myspotify.model.Playlist

/**
 * Playlist_menu - 歌单菜单页面
 * 点击歌单详情页右上角三点按钮弹出
 */
@Composable
fun PlaylistMenuView(
    playlist: Playlist,
    onBack: () -> Unit
) {
    val coverPath = if (playlist.id.startsWith("album")) {
        AssetMapper.albumCover(playlist.id)
    } else {
        AssetMapper.playlistCover(playlist.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF282828))
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // 顶部歌单信息
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AssetImage(
                assetPath = coverPath,
                contentDescription = playlist.name,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = playlist.name,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = playlist.creator,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(color = Color(0xFF535353), thickness = 0.5.dp)

        Spacer(modifier = Modifier.height(8.dp))

        // 菜单项列表
        PlaylistMenuItem(icon = Icons.Default.Add, text = "Add to Your Library")
        PlaylistMenuItem(icon = Icons.Default.ArrowDownward, text = "Download")
        PlaylistMenuItem(icon = Icons.Default.QueueMusic, text = "Add to queue")
        PlaylistMenuItem(icon = Icons.Default.Share, text = "Share")
        PlaylistMenuItem(icon = Icons.Default.Radio, text = "Go to radio")
        PlaylistMenuItem(icon = Icons.Default.Flag, text = "Report")

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
 * 歌单菜单项组件
 */
@Composable
private fun PlaylistMenuItem(icon: ImageVector, text: String) {
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
