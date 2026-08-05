package com.example.myspotify.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myspotify.data.AssetMapper
import com.example.myspotify.model.Song
import com.example.myspotify.ui.common.AssetImage
import kotlin.math.abs

/**
 * AboutTheArtistTab View - 艺术家介绍页面
 */
@Composable
fun AboutTheArtistTabView(song: Song? = null, onBack: () -> Unit) {
    val artistName = song?.artistName ?: "Kenshi Yonezu"
    val artistId = song?.artistId ?: "artist_005"
    val artistAvatar = AssetMapper.artistAvatar(artistId)

    val monthlyListeners = remember(artistId) {
        val hash = abs(artistId.hashCode())
        val base = (hash % 50 + 10) / 10.0
        "${base}M"
    }

    val artistBio = remember(artistName) {
        when (artistName) {
            "Taylor Swift" -> "$artistName is an American singer-songwriter who has become one of the most influential music artists of the 21st century. Known for her storytelling ability and genre-spanning music, she has sold over 200 million records worldwide.\n\nStarting as a country music artist, Swift transitioned to pop with her album \"1989\" and has continued to evolve her sound with each release. Her re-recording project, starting with \"Fearless (Taylor's Version)\", demonstrated her commitment to owning her artistic legacy.\n\nBeyond music, Swift is known for her advocacy for artists' rights and her philanthropic efforts. She continues to break records and push boundaries in the music industry."
            "Ed Sheeran" -> "$artistName is an English singer-songwriter and musician known for his heartfelt lyrics and acoustic-driven sound. Born in Halifax, West Yorkshire, he began writing songs at a young age.\n\nSheeran's breakthrough came with his debut album \"+\" (Plus) in 2011, featuring hits like \"The A Team.\" His subsequent albums have all achieved massive commercial success, with \"Shape of You\" becoming one of the best-selling digital singles of all time.\n\nHe is known for his live performances using loop pedals and his ability to fill stadiums with just an acoustic guitar. His songwriting skills have also made him one of the most sought-after collaborators in the music industry."
            "The Weeknd" -> "$artistName is a Canadian singer, songwriter, and record producer known for his distinctive vocal style and dark R&B sound. Born Abel Tesfaye, he initially gained recognition through anonymous uploads to YouTube.\n\nHis trilogy of mixtapes established him as a major force in contemporary R&B. Albums like \"Beauty Behind the Madness\" and \"After Hours\" produced chart-topping hits and critical acclaim.\n\nThe Weeknd's artistry extends beyond music into visual storytelling, with elaborate music videos and live performances. His Super Bowl halftime show performance was widely praised for its creativity."
            "Drake" -> "$artistName is a Canadian rapper, singer, songwriter, and actor who has become one of the most commercially successful artists in music history. Starting his career as an actor on \"Degrassi: The Next Generation,\" he transitioned to music.\n\nDrake's unique blend of hip-hop and R&B, combined with his emotional vulnerability in lyrics, created a new template for modern rap. Albums like \"Take Care,\" \"Nothing Was the Same,\" and \"Scorpion\" have all been critically and commercially successful.\n\nHe holds numerous streaming records and has influenced an entire generation of artists with his musical style."
            else -> "$artistName is a Japanese musician, singer-songwriter, record producer, and illustrator born on March 10, 1991 in Tokushima Prefecture, Japan. He first gained attention in the early 2010s as a Vocaloid producer under the name \"Hachi\", uploading original compositions to Niconico that quickly amassed millions of views.\n\nIn 2012, Yonezu transitioned to performing his own vocals and released his debut album \"diorama\" independently. His unique blend of pop, rock, and electronic elements, combined with deeply introspective lyrics, set him apart from his contemporaries. His major-label debut came in 2014 with the album \"YANKEE\", which debuted at number one on the Oricon charts.\n\nHis 2018 single \"Lemon\" became one of the best-selling digital singles in Japanese music history, surpassing 3 million downloads and accumulating over 800 million views on YouTube."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 顶部��
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
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
                text = "About the artist",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 可滚动内容
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 大尺寸艺术家图片
            AssetImage(
                assetPath = artistAvatar,
                contentDescription = artistName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // 艺术家���
                Text(
                    text = artistName,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 月听众数
                Text(
                    text = "$monthlyListeners monthly listeners",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Follow 按钮
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.height(36.dp),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White)
                ) {
                    Text(
                        text = "Follow",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 艺术家介绍
                Text(
                    text = artistBio,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            // ===== Posted by the artist =====
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Posted by the artist",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 帖子卡片
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF282828))
                        .padding(16.dp)
                ) {
                    // 艺术家头像 + 名字 + 日期
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AssetImage(
                            assetPath = artistAvatar,
                            contentDescription = artistName,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = artistName,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Dec 15, 2024",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 帖子内容
                    Text(
                        text = "Thank you all for your incredible support! New music is on the way. Stay tuned for updates!",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 帖子中的封面图
                    AssetImage(
                        assetPath = AssetMapper.songCover(song ?: Song("", "", "", "", "", "", 0L, "")),
                        contentDescription = "Album Cover",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            // ===== Fans also like =====
            Column(
                modifier = Modifier.padding(start = 24.dp)
            ) {
                Text(
                    text = "Fans also like",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val fansAlsoLike = listOf(
                        Pair("YOASOBI", "avatar/1.png"),
                        Pair("Ado", "avatar/2.png"),
                        Pair("Fujii Kaze", "avatar/3.png"),
                        Pair("Aimer", "avatar/4.png"),
                        Pair("RADWIMPS", "avatar/5.png")
                    )

                    fansAlsoLike.forEach { (name, avatar) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(100.dp)
                        ) {
                            AssetImage(
                                assetPath = avatar,
                                contentDescription = name,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = name,
                                color = Color.White,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}
