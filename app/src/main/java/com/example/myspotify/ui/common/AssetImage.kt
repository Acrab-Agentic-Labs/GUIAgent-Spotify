package com.example.myspotify.ui.common

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 从 assets 文件夹异步加载图片的可复用组件
 */
@Composable
fun AssetImage(
    assetPath: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    var bitmap by remember(assetPath) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(assetPath) {
        bitmap = withContext(Dispatchers.IO) {
            try {
                val options = BitmapFactory.Options().apply { inSampleSize = 4 }
                context.assets.open(assetPath).use { stream ->
                    BitmapFactory.decodeStream(stream, null, options)?.asImageBitmap()
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    if (bitmap != null) {
        Image(
            bitmap = bitmap!!,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        Box(
            modifier = modifier.background(Color(0xFF282828))
        )
    }
}
