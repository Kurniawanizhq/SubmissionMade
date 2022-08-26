package com.eone.core.domain.usecase

import com.eone.core.data.source.Resource
import com.eone.core.domain.model.Content
import kotlinx.coroutines.flow.Flow

interface ContentUseCase {

    fun getAllMovies(sort: String): Flow<Resource<List<Content>>>

    fun getAllTvShows(sort: String): Flow<Resource<List<Content>>>

    fun getFavoriteMovies(sort: String): Flow<List<Content>>

    fun getFavoriteTvShows(sort: String): Flow<List<Content>>

    fun getSearchMovies(search: String): Flow<List<Content>>

    fun getSearchTvShows(search: String): Flow<List<Content>>

    fun setMovieFavorite(content: Content, state: Boolean)
}