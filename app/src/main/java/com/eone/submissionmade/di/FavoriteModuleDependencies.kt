package com.eone.submissionmade.di

import com.eone.core.domain.usecase.ContentUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FavoriteModuleDependencies {

    fun contentUseCase(): ContentUseCase
}