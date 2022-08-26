package com.eone.core.di

import com.eone.core.data.source.ContentRepository
import com.eone.core.domain.repository.IContentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class, DatabaseModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideRepository(contentRepository: ContentRepository): IContentRepository

}