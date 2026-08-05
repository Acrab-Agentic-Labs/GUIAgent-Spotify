package com.example.myspotify.model

/**
 * 歌手数据模型
 */
data class Artist(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val bio: String = "",
    val genres: List<String> = emptyList()
)
