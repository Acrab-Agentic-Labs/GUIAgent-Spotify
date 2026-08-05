package com.example.myspotify.ui.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * PaymentSuccessView - 付费成功页面
 */
@Composable
fun PaymentSuccessView(
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 成功图标
        Surface(
            modifier = Modifier.size(120.dp),
            color = Color(0xFF1DB954),
            shape = CircleShape
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success",
                    tint = Color.White,
                    modifier = Modifier.size(80.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 成功标题
        Text(
            text = "Welcome to Premium!",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 说明文字
        Text(
            text = "Your Premium subscription is now active. Enjoy ad-free music, offline listening, and more!",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp,
            lineHeight = 22.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // 完成按钮
        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1DB954)
            )
        ) {
            Text(
                text = "Start Listening",
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
