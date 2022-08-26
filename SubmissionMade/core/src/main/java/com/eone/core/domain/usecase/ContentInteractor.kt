package com.eone.core.domain.usecase

import com.eone.core.data.source.Resource
import com.eone.core.domain.repository.IContentRepository
import com.eone.core.domain.model.Content
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContentInteractor @Inject constructor(private val contentRepository: IContentRepository):
    ContentUseCase {
    override fun getAllMovies(sort: String): Flow<Resource<List<Content>>> = contentRepository.getAllMovies(sort)

    override fun getAllTvShows(sort: String): Flow<Resource<List<Content>>> = contentRepository.getAllTvShows(sort)

    override fun getFavoriteMovies(sort: String): Flow<List<Content>> = contentRepository.getFavoriteMovies(sort)

    override fun getFavoriteTvShows(sort: String): Flow<List<Content>> = contentRepository.getFavoriteTvShows(sort)

    override fun getSearchMovies(search: String): Flow<List<Content>> = contentRepository.getSearchMovies(search)

    override fun getSearchTvShows(search: String): Flow<List<Content>> = contentRepository.getSearchTvShows(search)

    override fun setMovieFavorite(content: Content, state: Boolean) = contentRepository.setMovieFavorite(content, state)
}