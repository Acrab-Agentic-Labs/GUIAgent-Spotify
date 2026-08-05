package com.example.myspotify.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.myspotify.ui.common.AssetImage

private data class VenueItem(val name: String, val location: String, val coverPath: String)

private val venues = listOf(
    VenueItem("Madison Square Garden", "New York, NY", "cover/4.png"),
    VenueItem("The Forum", "Los Angeles, CA", "cover/5.png"),
    VenueItem("Red Rocks Amphitheatre", "Morrison, CO", "cover/6.png"),
    VenueItem("Coachella Music Festival", "Indio, CA", "cover/7.png")
)

/**
 * AddEventsAndVenuesTab - 添加活动和场馆页面
 * 从 YourLibraryTab 新增入口进入
 */
@Composable
fun AddEventsAndVenuesTabView(
    onBack: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

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
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Add events and venues",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 搜索框
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = {
                Text("Search", color = Color.Gray, fontSize = 14.sp)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF282828),
                unfocusedContainerColor = Color(0xFF282828),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 标题
        Text(
            text = "Events and venues near you",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 场馆列表
        LazyColumn {
            items(venues) { venue ->
                VenueListItem(venue = venue)
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

/**
 * 场馆列表项（带 Follow 按钮）
 */
@Composable
private fun VenueListItem(venue: VenueItem) {
    var isFollowed by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 方形图片
        AssetImage(
            assetPath = venue.coverPath,
            contentDescription = venue.name,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 名称和位置
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = venue.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = venue.location,
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        // Follow 按钮
        OutlinedButton(
            onClick = { isFollowed = !isFollowed },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (isFollowed) Color(0xFF1DB954) else Color.Transparent,
                contentColor = if (isFollowed) Color.Black else Color.White
            ),
            border = if (isFollowed) null else ButtonDefaults.outlinedButtonBorder(enabled = true),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (isFollowed) "Following" else "Follow",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
