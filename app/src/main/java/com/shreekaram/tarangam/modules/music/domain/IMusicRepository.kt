package com.shreekaram.tarangam.modules.music.domain

import kotlinx.coroutines.flow.Flow

interface IMusicRepository {
    suspend fun getMusic(): Flow<Music>
    fun dispose(): Unit
}