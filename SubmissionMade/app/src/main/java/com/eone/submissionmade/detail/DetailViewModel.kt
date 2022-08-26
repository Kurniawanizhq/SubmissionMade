package com.eone.submissionmade.detail

import androidx.lifecycle.ViewModel
import com.eone.core.domain.model.Content
import com.eone.core.domain.usecase.ContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor (private val contentUseCase: ContentUseCase): ViewModel() {

    fun setFavoriteMovie(content: Content, newStatus: Boolean) {
        contentUseCase.setMovieFavorite(content, newStatus)
    }

}