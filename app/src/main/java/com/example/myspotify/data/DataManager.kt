package com.example.myspotify.data

import android.content.Context
import com.example.myspotify.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 数据管理类 - 负责加载和管理所有数据
 */
class DataManager(private val context: Context) {
    private val gson = Gson()

    // 缓存数据
    private var songs: List<Song>? = null
    private var artists: List<Artist>? = null
    private var albums: List<Album>? = null
    private var playlists: List<Playlist>? = null
    private var userData: UserData? = null

    /**
     * 从 assets 加载 JSON 文件
     */
    private fun loadJsonFromAssets(fileName: String): String {
        return context.assets.open("data/$fileName").bufferedReader().use { it.readText() }
    }

    /**
     * 获取所有歌曲
     */
    fun getSongs(): List<Song> {
        if (songs == null) {
            val json = loadJsonFromAssets("songs.json")
            songs = gson.fromJson(json, object : TypeToken<List<Song>>() {}.type)
        }
        return songs ?: emptyList()
    }

    /**
     * 获取所有歌手
     */
    fun getArtists(): List<Artist> {
        if (artists == null) {
            val json = loadJsonFromAssets("artists.json")
            artists = gson.fromJson(json, object : TypeToken<List<Artist>>() {}.type)
        }
        return artists ?: emptyList()
    }

    /**
     * 获取所有专辑
     */
    fun getAlbums(): List<Album> {
        if (albums == null) {
            val json = loadJsonFromAssets("albums.json")
            albums = gson.fromJson(json, object : TypeToken<List<Album>>() {}.type)
        }
        return albums ?: emptyList()
    }

    /**
     * 获取所有歌单
     */
    fun getPlaylists(): List<Playlist> {
        if (playlists == null) {
            val json = loadJsonFromAssets("playlists.json")
            playlists = gson.fromJson(json, object : TypeToken<List<Playlist>>() {}.type)
        }
        return playlists ?: emptyList()
    }

    /**
     * 根据 ID 获取歌曲
     */
    fun getSongById(id: String): Song? {
        return getSongs().find { it.id == id }
    }

    /**
     * 根据 ID 获取歌手
     */
    fun getArtistById(id: String): Artist? {
        return getArtists().find { it.id == id }
    }

    /**
     * 获取用户数据
     */
    fun getUserData(): UserData {
        if (userData == null) {
            val json = loadJsonFromAssets("user_data.json")
            userData = gson.fromJson(json, UserData::class.java)
        }
        return userData ?: UserData()
    }

    /**
     * 根据 ID 获取歌单
     */
    fun getPlaylistById(id: String): Playlist? {
        return getPlaylists().find { it.id == id }
    }
}
