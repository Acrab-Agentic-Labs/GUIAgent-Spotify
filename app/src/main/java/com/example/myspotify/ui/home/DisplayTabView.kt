package com.example.myspotify.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * DisplayTab - 设置与显示页面（从 UserTab 进入）
 */
@Composable
fun DisplayTabView(onBack: () -> Unit) {
    var canvasEnabled by remember { mutableStateOf(true) }
    var autoplayEnabled by remember { mutableStateOf(true) }
    var showUnplayable by remember { mutableStateOf(false) }
    var explicitContent by remember { mutableStateOf(true) }
    var normalizeVolume by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 顶部栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "Settings",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // "Account" 区域
            item {
                SettingsSectionHeader(title = "Account")
            }
            item {
                SettingsClickItem(
                    icon = Icons.Default.AccountCircle,
                    title = "Free plan",
                    subtitle = "View available plans"
                )
            }
            item {
                SettingsClickItem(
                    icon = Icons.Default.Email,
                    title = "Email",
                    subtitle = "user@example.com"
                )
            }

            // "Display" 区域
            item {
                SettingsSectionHeader(title = "Display")
            }
            item {
                SettingsToggleItem(
                    title = "Canvas",
                    subtitle = "Show visual loops on tracks",
                    checked = canvasEnabled,
                    onCheckedChange = { canvasEnabled = it }
                )
            }
            item {
                SettingsToggleItem(
                    title = "Show unplayable songs",
                    subtitle = "Show songs that are not available",
                    checked = showUnplayable,
                    onCheckedChange = { showUnplayable = it }
                )
            }

            // "Playback" 区域
            item {
                SettingsSectionHeader(title = "Playback")
            }
            item {
                SettingsToggleItem(
                    title = "Autoplay",
                    subtitle = "Continue playing similar content",
                    checked = autoplayEnabled,
                    onCheckedChange = { autoplayEnabled = it }
                )
            }
            item {
                SettingsToggleItem(
                    title = "Allow explicit content",
                    subtitle = "Turn on to play explicit content",
                    checked = explicitContent,
                    onCheckedChange = { explicitContent = it }
                )
            }
            item {
                SettingsToggleItem(
                    title = "Normalize volume",
                    subtitle = "Set the same volume level for all tracks",
                    checked = normalizeVolume,
                    onCheckedChange = { normalizeVolume = it }
                )
            }

            // "Audio Quality" 区域
            item {
                SettingsSectionHeader(title = "Audio Quality")
            }
            item {
                SettingsClickItem(
                    icon = Icons.Default.PlayArrow,
                    title = "Streaming quality",
                    subtitle = "Automatic"
                )
            }
            item {
                SettingsClickItem(
                    icon = Icons.Default.ArrowBack,
                    title = "Download quality",
                    subtitle = "Normal"
                )
            }

            // "About" 区域
            item {
                SettingsSectionHeader(title = "About")
            }
            item {
                SettingsClickItem(
                    icon = Icons.Default.Info,
                    title = "Version",
                    subtitle = "1.0.0"
                )
            }

            // 底部间距
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

/**
 * 设置区域标题
 */
@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    )
}

/**
 * 带开关的设置项
 */
@Composable
fun SettingsToggleItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp
            )
            Text(
                text = subtitle,
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = Color(0xFF1DB954),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color(0xFF282828)
            )
        )
    }
}

/**
 * 可点击的设置项
 */
@Composable
fun SettingsClickItem(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp
            )
            Text(
                text = subtitle,
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}
