package com.example.myspotify.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * PlaylistNameTab - 给歌单取名的页面
 * 从 CreateTab 选择 Playlist 后进入
 */
@Composable
fun PlaylistNameTabView(
    onCancel: () -> Unit,
    onCreate: (String) -> Unit
) {
    var playlistName by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF282828)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // 标题
        Text(
            text = "Give your playlist a name.",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 输入框
        BasicTextField(
            value = playlistName,
            onValueChange = { playlistName = it },
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            cursorBrush = SolidColor(Color.White),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .focusRequester(focusRequester),
            decorationBox = { innerTextField ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.Center) {
                        if (playlistName.isEmpty()) {
                            Text(
                                text = "My Playlist",
                                color = Color.Gray,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        innerTextField()
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        color = Color.White,
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        // 底部按钮
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Cancel 按钮
            TextButton(onClick = onCancel) {
                Text(
                    text = "Cancel",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Create 按钮
            Button(
                onClick = {
                    val name = playlistName.ifBlank { "My Playlist" }
                    onCreate(name)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (playlistName.isNotBlank()) Color(0xFF1DB954) else Color(0xFF535353)
                ),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Create",
                    color = if (playlistName.isNotBlank()) Color.Black else Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
