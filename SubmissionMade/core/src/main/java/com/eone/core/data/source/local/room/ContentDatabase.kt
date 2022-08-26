package com.eone.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eone.core.data.source.local.entity.ContentEntity

@Database(entities = [ContentEntity::class], version = 1, exportSchema = false)
abstract class ContentDatabase : RoomDatabase() {
    abstract fun movieDao(): ContentDao
}