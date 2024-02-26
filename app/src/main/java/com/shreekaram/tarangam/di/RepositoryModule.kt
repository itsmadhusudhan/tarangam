package com.shreekaram.tarangam.di

import android.content.Context
import com.shreekaram.tarangam.modules.music.data.MusicRepository
import com.shreekaram.tarangam.modules.music.data.utils.MusicHelper
import com.shreekaram.tarangam.modules.music.domain.IMusicRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMusicRepository(repository: MusicRepository): IMusicRepository
}