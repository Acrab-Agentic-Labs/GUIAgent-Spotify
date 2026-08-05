package com.example.myspotify.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.myspotify.ui.common.AssetImage

/**
 * UserTab - 用户个人页面（点击首页头像进入）
 */
@Composable
fun UserTabView(onBack: () -> Unit) {
    val menuItems = listOf(
        UserMenuItem(Icons.Default.Add, "Add account"),
        UserMenuItem(Icons.Default.Notifications, "What's new"),
        UserMenuItem(Icons.Default.Refresh, "Recents"),
        UserMenuItem(Icons.Default.List, "Your Library"),
        UserMenuItem(Icons.Default.Settings, "Settings and privacy")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        // 顶部返回按钮
        IconButton(
            onClick = onBack,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White
            )
        }

        // 用户头像和信息
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AssetImage(
                assetPath = AssetMapper.userAvatar,
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "User",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "View profile",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.clickable { }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 菜单列表
        LazyColumn {
            items(menuItems) { item ->
                UserMenuItemRow(item)
            }
        }
    }
}

/**
 * 菜单项数据
 */
data class UserMenuItem(
    val icon: ImageVector,
    val title: String
)

/**
 * 菜单项行组件
 */
@Composable
fun UserMenuItemRow(item: UserMenuItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = item.title,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}
