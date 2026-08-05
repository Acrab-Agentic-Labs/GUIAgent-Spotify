package com.example.myspotify.model

/**
 * 播放模式枚举
 */
enum class PlayMode {
    SEQUENTIAL,  // 顺序播放
    REPEAT_ONE,  // 单曲循环
    REPEAT_ALL,  // 列表循环
    SHUFFLE      // 随机播放
}

/**
 * 播放状态数据模型
 * 用于 GUI Agent 任务检查
 */
data class PlaybackState(
    val currentSongId: String? = null,
    val isPlaying: Boolean = false,
    val progressMs: Long = 0,
    val playMode: PlayMode = PlayMode.SEQUENTIAL,
    val playlistQueue: List<String> = emptyList(),
    val currentIndex: Int = 0
)
