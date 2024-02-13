package com.shreekaram.tarangam.modules.songs.domain

import android.os.Parcelable
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val path: String,
    @IgnoredOnParcel
    val image: ImageBitmap? = null,
) : Parcelable


data class Music(
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val path: String,
    val image: ImageBitmap? = null,
//    title with extension
    val rawTitle: String,
)