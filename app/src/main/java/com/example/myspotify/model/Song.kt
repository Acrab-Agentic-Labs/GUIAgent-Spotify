package com.example.myspotify.model

/**
 * 歌曲数据模型
 */
data class Song(
    val id: String,
    val title: String,
    val artistId: String,
    val artistName: String,
    val albumId: String,
    val albumName: String,
    val durationMs: Long,
    val coverUrl: String,
    val audioFilePath: String = ""
)
