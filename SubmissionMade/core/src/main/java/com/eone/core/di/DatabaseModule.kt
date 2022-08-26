package com.eone.core.di

import android.content.Context
import androidx.room.Room
import com.eone.core.data.source.local.room.ContentDao
import com.eone.core.data.source.local.room.ContentDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideContentDao(contentDatabase: ContentDatabase): ContentDao = contentDatabase.movieDao()

    @Provides
    @Singleton
    fun provideContentDatabase(@ApplicationContext context: Context): ContentDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ContentDatabase::class.java,
            "Content.db"
        ).build()
    }
}