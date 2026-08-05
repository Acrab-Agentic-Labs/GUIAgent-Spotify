package com.example.myspotify.model

/**
 * 用户数据模型
 * 用于 GUI Agent 任务检查
 */
data class UserData(
    val likedSongs: MutableList<String> = mutableListOf(),
    val savedPlaylists: MutableList<String> = mutableListOf(),
    val followedArtists: MutableList<String> = mutableListOf(),
    val recentlyPlayed: MutableList<String> = mutableListOf()
)
