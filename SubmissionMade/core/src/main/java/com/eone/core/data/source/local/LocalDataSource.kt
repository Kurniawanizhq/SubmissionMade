package com.eone.core.data.source.local

import com.eone.core.data.source.local.entity.ContentEntity
import com.eone.core.data.source.local.room.ContentDao
import com.eone.core.utils.SortUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val mContentDao: ContentDao) {

    fun getAllMovies(sort: String): Flow<List<ContentEntity>> {
        val query = SortUtils.getSortedQueryMovies(sort)
        return mContentDao.getMovies(query)
    }

    fun getAllTvShows(sort: String): Flow<List<ContentEntity>> {
        val query = SortUtils.getSortedQueryTvShows(sort)
        return mContentDao.getTvShows(query)
    }

    fun getAllFavoriteMovies(sort: String): Flow<List<ContentEntity>> {
        val query = SortUtils.getSortedQueryFavoriteMovies(sort)
        return mContentDao.getFavoriteMovies(query)
    }

    fun getAllFavoriteTvShows(sort: String): Flow<List<ContentEntity>> {
        val query = SortUtils.getSortedQueryFavoriteTvShows(sort)
        return mContentDao.getFavoriteTvShows(query)
    }

    fun getMovieSearch(search: String): Flow<List<ContentEntity>> {
        return mContentDao.getSearchMovies(search)
            .flowOn(Dispatchers.Default)
            .conflate()
    }

    fun getTvShowSearch(search: String): Flow<List<ContentEntity>> {
        return mContentDao.getSearchTvShows(search)
            .flowOn(Dispatchers.Default)
            .conflate()
    }

    suspend fun insertMovies(movies: List<ContentEntity>) = mContentDao.insertMovie(movies)

    fun setMovieFavorite(movie: ContentEntity, newState: Boolean) {
        movie.favorite = newState
        mContentDao.updateFavoriteMovie(movie)
    }
}