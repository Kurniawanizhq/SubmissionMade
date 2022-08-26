package com.eone.submissionmade.di


import com.eone.core.domain.usecase.ContentInteractor
import com.eone.core.domain.usecase.ContentUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideContentUseCase(contentInteractor: ContentInteractor): ContentUseCase

}
