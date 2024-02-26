package com.shreekaram.tarangam.di

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import com.shreekaram.tarangam.modules.music.data.utils.MusicHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideResources(@ApplicationContext context: Context): Resources? {
        return context.resources
    }

    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @Singleton
    fun provideMusicHelper(@ApplicationContext context: Context): MusicHelper {
        return MusicHelper(context.cacheDir.absolutePath)
    }
}