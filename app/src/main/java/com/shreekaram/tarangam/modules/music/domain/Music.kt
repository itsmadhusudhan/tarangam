package com.shreekaram.tarangam.modules.music.domain

data class Music(
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val albumArt: String?,
    val path: String
)
