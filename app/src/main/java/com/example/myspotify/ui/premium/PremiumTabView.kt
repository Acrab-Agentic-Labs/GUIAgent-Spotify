package com.example.myspotify.ui.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * PremiumTab View - Premium 页面视图
 */
@Composable
fun PremiumTabView(
    onPremiumActivated: () -> Unit = {},
    onPlaceholderClick: () -> Unit = {}
) {
    var showCheckOutTab by remember { mutableStateOf(false) }
    var showPaymentSuccess by remember { mutableStateOf(false) }

    if (showPaymentSuccess) {
        PaymentSuccessView(
            onDone = {
                showPaymentSuccess = false
                showCheckOutTab = false
                onPremiumActivated()
            }
        )
        return
    }

    if (showCheckOutTab) {
        CheckOutTabView(
            onBack = { showCheckOutTab = false },
            onPaymentSuccess = { showPaymentSuccess = true },
            onPlaceholderClick = onPlaceholderClick
        )
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2E1A47),
                        Color(0xFF5B3A70),
                        Color(0xFFB8860B),
                        Color.Black
                    )
                )
            )
    ) {
        item {
            Spacer(modifier = Modifier.height(60.dp))
        }

        // Premium Logo
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Spotify",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Premium",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // 主标题
        item {
            Text(
                text = "Get 3 months of",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Text(
                text = "Premium for \$0 with",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Text(
                text = "Spotify*",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Limited time offer 标签
        item {
            Surface(
                modifier = Modifier.padding(horizontal = 32.dp),
                color = Color(0xFF1E1E1E),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔔",
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Limited time offer",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Try 按钮
        item {
            Button(
                onClick = { showCheckOutTab = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Try 3 months for \$0",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 说明文字
        item {
            Text(
                text = "Free for 3 months, then \$12.99 per month after. Offer only available if you haven't tried Premium before and you subscribe via Spotify. Offers via Google Play may differ. Terms apply. Offer ends March 31, 2026.",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(48.dp))
        }

        // "Why join Premium?" 标题
        item {
            Text(
                text = "Why join Premium?",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // 功能列表
        item {
            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                PremiumFeature(
                    icon = Icons.Default.Info,
                    title = "Ad-free music listening"
                )
                PremiumFeature(
                    icon = Icons.Default.ArrowBack,
                    title = "Download to listen offline"
                )
                PremiumFeature(
                    icon = Icons.Default.PlayArrow,
                    title = "Play songs in any order"
                )
                PremiumFeature(
                    icon = Icons.Default.Star,
                    title = "High audio quality"
                )
            }
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

/**
 * Premium 功能项组件
 */
@Composable
fun PremiumFeature(icon: ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}
