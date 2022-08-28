package com.eone.submissionmade.content

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.eone.core.data.source.Resource
import com.eone.core.domain.model.Content
import com.eone.core.domain.usecase.ContentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class ContentViewModel @Inject constructor(private val contentUseCase : ContentUseCase): ViewModel() {
    private val timeoutMilis = 300L

    fun getMovies(sort: String): LiveData<Resource<List<Content>>> = contentUseCase.getAllMovies(sort).asLiveData()

    fun getTvShows(sort: String): LiveData<Resource<List<Content>>> = contentUseCase.getAllTvShows(sort).asLiveData()

    val queryChannel = MutableStateFlow("")

    var movieResult = queryChannel
        .debounce(timeoutMilis)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .flatMapLatest {
            contentUseCase.getSearchMovies(it)
        }.asLiveData()

    val tvShowResult = queryChannel
        .debounce(timeoutMilis)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .flatMapLatest {
            contentUseCase.getSearchTvShows(it)
        }.asLiveData()
}