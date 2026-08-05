package com.example.myspotify.data

import android.content.Context
import android.util.Log
import com.example.myspotify.model.Playlist
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

/**
 * AutoTest 状态导出器
 * 将应用运行时状态写入 app 私有目录的 autotest/ 下，供外部 check 脚本通过 adb 读取
 *
 * 使用方式（adb 读取）：
 *   adb exec-out run-as com.example.myspotify cat files/autotest/user_state.json
 */
class AutoTestExporter(private val context: Context) {

    companion object {
        private const val TAG = "AutoTestExporter"
        private const val DIR_NAME = "autotest"
    }

    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    private val autotestDir: File by lazy {
        File(context.filesDir, DIR_NAME).also {
            if (!it.exists()) it.mkdirs()
        }
    }

    private fun writeJson(fileName: String, data: Any) {
        try {
            val file = File(autotestDir, fileName)
            file.writeText(gson.toJson(data))
        } catch (e: Exception) {
            Log.e(TAG, "Failed to write $fileName: ${e.message}")
        }
    }

    /**
     * 导出用户状态（liked songs, followed artists, saved podcasts/audiobooks）
     */
    fun exportUserState(
        likedSongIds: List<String>,
        followedArtistIds: List<String>,
        savedPodcastIds: List<String>,
        savedAudiobookIds: List<String>,
        savedPlaylistIds: List<String>
    ) {
        val state = mapOf(
            "likedSongs" to likedSongIds,
            "followedArtists" to followedArtistIds,
            "savedPodcasts" to savedPodcastIds,
            "savedAudiobooks" to savedAudiobookIds,
            "savedPlaylists" to savedPlaylistIds
        )
        writeJson("user_state.json", state)
    }

    /**
     * 导出用户创建的歌单
     */
    fun exportUserPlaylists(userPlaylists: List<Playlist>) {
        val data = userPlaylists.map { playlist ->
            mapOf(
                "id" to playlist.id,
                "name" to playlist.name,
                "creator" to playlist.creator,
                "songIds" to playlist.songIds
            )
        }
        writeJson("user_playlists.json", data)
    }

    /**
     * 导出会员状态
     */
    fun exportPremiumState(
        isPremium: Boolean,
        subscriptionDate: String? = null
    ) {
        val state = mapOf(
            "isPremium" to isPremium,
            "subscriptionDate" to subscriptionDate
        )
        writeJson("premium_state.json", state)
    }

    /**
     * 导出最近搜索状态
     */
    fun exportSearchState(
        initialItems: List<Map<String, String>>,
        currentItems: List<Map<String, String>>
    ) {
        val currentIds = currentItems.mapNotNull { it["id"] }.toSet()
        val removedItems = initialItems.filter { item ->
            val id = item["id"]
            id != null && id !in currentIds
        }
        val state = mapOf(
            "initialItems" to initialItems,
            "currentItems" to currentItems,
            "removedItems" to removedItems,
            "initialCount" to initialItems.size,
            "currentCount" to currentItems.size,
            "removedCount" to removedItems.size
        )
        writeJson("search_state.json", state)
    }

    /**
     * 导出播放状态
     */
    fun exportPlaybackState(
        currentSongId: String?,
        isPlaying: Boolean,
        playMode: String,
        shuffleEnabled: Boolean,
        sleepTimerDuration: String? = null
    ) {
        val state = mapOf(
            "currentSongId" to currentSongId,
            "isPlaying" to isPlaying,
            "playMode" to playMode,
            "shuffleEnabled" to shuffleEnabled,
            "sleepTimerDuration" to sleepTimerDuration
        )
        writeJson("playback_state.json", state)
    }

    /**
     * 导出全部状态（一次性调用）
     */
    fun exportAll(
        likedSongIds: List<String>,
        followedArtistIds: List<String>,
        savedPodcastIds: List<String>,
        savedAudiobookIds: List<String>,
        savedPlaylistIds: List<String>,
        userPlaylists: List<Playlist>,
        currentSongId: String?,
        isPlaying: Boolean,
        playMode: String,
        shuffleEnabled: Boolean,
        sleepTimerDuration: String? = null
    ) {
        exportUserState(likedSongIds, followedArtistIds, savedPodcastIds, savedAudiobookIds, savedPlaylistIds)
        exportUserPlaylists(userPlaylists)
        exportPlaybackState(currentSongId, isPlaying, playMode, shuffleEnabled, sleepTimerDuration)
    }
}
