package com.example.myspotify.data

import com.example.myspotify.model.Song

/**
 * ID 到 assets 图片路径的映射
 */
object AssetMapper {

    val userAvatar = "avatar/6.png"

    private val albumCoverMap = mapOf(
        "album_001" to "cover/1.png",
        "album_002" to "cover/2.png",
        "album_003" to "cover/3.png",
        "album_004" to "cover/8.png",
        "album_005" to "cover/9.png"
    )

    private val playlistCoverMap = mapOf(
        "playlist_001" to "cover/4.png",
        "playlist_002" to "cover/5.png",
        "playlist_003" to "cover/6.png",
        "playlist_004" to "cover/7.png",
        "mfy_001" to "cover/8.png",
        "mfy_002" to "cover/9.png",
        "mfy_003" to "cover/10.png",
        "mfy_004" to "cover/11.png",
        "mfy_005" to "cover/12.png"
    )

    private val artistAvatarMap = mapOf(
        "artist_001" to "avatar/1.png",
        "artist_002" to "avatar/2.png",
        "artist_003" to "avatar/3.png",
        "artist_004" to "avatar/4.png",
        "artist_005" to "avatar/5.png",
        "artist_006" to "avatar/7.png"
    )

    fun albumCover(albumId: String): String =
        albumCoverMap[albumId] ?: "cover/10.png"

    fun playlistCover(playlistId: String): String =
        playlistCoverMap[playlistId] ?: "cover/10.png"

    fun artistAvatar(artistId: String): String =
        artistAvatarMap[artistId] ?: "avatar/7.png"

    private val songCoverMap = mapOf(
        "song_001" to "cover/1.png",
        "song_002" to "cover/14.png",
        "song_003" to "cover/15.png",
        "song_004" to "cover/2.png",
        "song_005" to "cover/11.png",
        "song_006" to "cover/12.png",
        "song_007" to "cover/3.png",
        "song_008" to "cover/10.png",
        "song_009" to "cover/9.png",
        "song_010" to "cover/8.png",
        "song_011" to "cover/13.png",
        "song_012" to "cover/5.png",
        "song_013" to "cover/13.png"
    )

    fun songCover(song: Song): String =
        songCoverMap[song.id] ?: albumCover(song.albumId)
}
