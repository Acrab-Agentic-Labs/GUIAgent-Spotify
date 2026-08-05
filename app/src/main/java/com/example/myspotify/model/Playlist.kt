package com.example.myspotify.model

/**
 * 歌单数据模型
 */
data class Playlist(
    val id: String,
    val name: String,
    val coverUrl: String,
    val creator: String = "Spotify",
    val description: String = "",
    val songIds: List<String> = emptyList()
)
