package com.eone.core.data.source.local.room

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.eone.core.data.source.local.entity.ContentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {

    @RawQuery(observedEntities = [ContentEntity::class])
    fun getMovies(query: SupportSQLiteQuery): Flow<List<ContentEntity>>

    @RawQuery(observedEntities = [ContentEntity::class])
    fun getTvShows(query: SupportSQLiteQuery): Flow<List<ContentEntity>>

    @Query("SELECT * FROM movieEntities WHERE isTvShow = 0 AND title LIKE '%' || :search || '%'")
    fun getSearchMovies(search: String): Flow<List<ContentEntity>>

    @Query("SELECT * FROM movieEntities WHERE isTvShow = 1 AND title LIKE '%' || :search || '%'")
    fun getSearchTvShows(search: String): Flow<List<ContentEntity>>

    @RawQuery(observedEntities = [ContentEntity::class])
    fun getFavoriteMovies(query: SupportSQLiteQuery): Flow<List<ContentEntity>>

    @RawQuery(observedEntities = [ContentEntity::class])
    fun getFavoriteTvShows(query: SupportSQLiteQuery): Flow<List<ContentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movies: List<ContentEntity>)

    @Update
    fun updateFavoriteMovie(movie: ContentEntity)
}