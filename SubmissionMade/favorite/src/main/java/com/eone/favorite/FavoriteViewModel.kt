package com.eone.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.eone.core.domain.model.Content
import com.eone.core.domain.usecase.ContentUseCase

class FavoriteViewModel (private val contentUseCase: ContentUseCase): ViewModel() {

    fun getFavoriteMovies(sort: String): LiveData<List<Content>> =
        contentUseCase.getFavoriteMovies(sort).asLiveData()

    fun getFavoriteTvShows(sort: String): LiveData<List<Content>> =
        contentUseCase.getFavoriteTvShows(sort).asLiveData()

    fun setFavorite(Content: Content, newState: Boolean) {
        contentUseCase.setMovieFavorite(Content, newState)
    }

}