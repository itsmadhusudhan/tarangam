package com.shreekaram.tarangam.modules.music.data.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.shreekaram.tarangam.modules.music.domain.Music
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MusicHelper(
    private val cacheDir: String
) {
    fun getAlbumArt(file: File): String? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(file.absolutePath)

        val image = retriever.embeddedPicture?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }

        // Basically we are caching the album art to the local cache as image file
        val imageFile =
            File(cacheDir, file.nameWithoutExtension + ".jpg")

        // save only if the file doesn't exist
        if (!imageFile.exists()) {
            image?.let { bitmap ->
                try {
                    FileOutputStream(imageFile).use { out ->
                        bitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            100,
                            out
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        return imageFile.absolutePath
    }

    fun getMusicWithMetadata(file: File): Music {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(file.absolutePath)

        val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
        val album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val image = retriever.embeddedPicture?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }

        // Basically we are caching the album art to the local cache as image file
        val imageFile =
            File(cacheDir, file.nameWithoutExtension + ".jpg")

        // save only if the file doesn't exist
        if (!imageFile.exists()) {
            image?.let { bitmap ->
                try {
                    FileOutputStream(imageFile).use { out ->
                        bitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            100,
                            out
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        return Music(
            title = title ?: file.nameWithoutExtension,
            artist = artist ?: "Unknown",
            album = album ?: "Unknown",
            duration = duration?.toLong() ?: 0,
            path = file.absolutePath,
            albumArt = imageFile.absolutePath,
        )
    }
}

