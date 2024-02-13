package com.shreekaram.tarangam.modules.songs.domain

interface ISongRepository {
    suspend fun getSongs(): MutableList<Song>
}