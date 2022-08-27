package com.eone.core.di

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.eone.core.R
import com.eone.core.data.source.local.room.ContentDao
import com.eone.core.data.source.local.room.ContentDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideContentDao(contentDatabase: ContentDatabase): ContentDao = contentDatabase.movieDao()

    private val passphrase: ByteArray = SQLiteDatabase.getBytes("eone".toCharArray())
    private val factory = SupportFactory(passphrase)

    @Provides
    @Singleton
    fun provideContentDatabase(@ApplicationContext context: Context): ContentDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ContentDatabase::class.java,
            context.getString(R.string.databaseName)
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}