package com.example.myspotify.model

/**
 * 专辑数据模型
 */
data class Album(
    val id: String,
    val name: String,
    val artistId: String,
    val artistName: String,
    val coverUrl: String,
    val releaseYear: Int,
    val songIds: List<String> = emptyList()
)
