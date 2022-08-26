package com.eone.favorite.di

import android.content.Context
import com.eone.favorite.movies.FavoriteMovieFragment
import com.eone.favorite.tvshows.FavoriteTvShowFragment
import com.eone.submissionmade.di.FavoriteModulDependencies
import dagger.BindsInstance
import dagger.Component

@Component(dependencies = [FavoriteModulDependencies::class])
interface FavoriteComponent {

    fun injectMovies(fragment: FavoriteMovieFragment)
    fun injectTvShow(fragment: FavoriteTvShowFragment)


    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(mapsModuleDependencies: FavoriteModulDependencies): Builder
        fun build(): FavoriteComponent
    }

}