package com.shreekaram.tarangam.modules.music.data

import java.io.File
import java.io.FileFilter

class MusicFilter : FileFilter {
    override fun accept(file: File): Boolean {
        val isMusicFile = file.extension.let {
            it == "mp3" || it == "wav" || it == "flac" || it == "m4a" || it == "aac" || it == "ogg" || it == "wma"
        }

        return !file.isDirectory && !file.isHidden && isMusicFile
    }
}