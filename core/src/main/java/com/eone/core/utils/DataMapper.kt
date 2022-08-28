package com.eone.core.utils

import com.eone.core.data.source.local.entity.ContentEntity
import com.eone.core.data.source.remote.response.MovieResponse
import com.eone.core.data.source.remote.response.TvShowResponse
import com.eone.core.domain.model.Content

object DataMapper {

    fun mapMovieResponsesToEntities(input: List<MovieResponse>): List<ContentEntity> {
        val movieList = ArrayList<ContentEntity>()
        input.map {
            val movie = ContentEntity(
                it.overview ?: "",
                it.originalLanguage ?: "",
                it.releaseDate ?: "",
                it.popularity  ?: 0.0,
                it.voteAverage ?: 0.0,
                it.id ?: 0,
                it.title ?: "",
                it.voteCount ?: 0,
                it.posterPath ?: "",
                favorite = false,
                isTvShows = false
            )
            movieList.add(movie)
        }
        return movieList
    }

    fun mapTvShowResponsesToEntities(input: List<TvShowResponse>): List<ContentEntity> {
        val movieList = ArrayList<ContentEntity>()
        input.map {
            val movie = ContentEntity(
                it.overview ?: "",
                it.originalLanguage ?: "",
                it.firstAirDate ?: "",
                it.popularity  ?: 0.0,
                it.voteAverage ?: 0.0,
                it.id ?: 0,
                it.name ?: "",
                it.voteCount ?: 0,
                it.posterPath ?: "",
                favorite = false,
                isTvShows = true
            )
            movieList.add(movie)
        }
        return movieList
    }

    fun mapEntitiesToDomain(input: List<ContentEntity>): List<Content> {
        return input.map {
            Content(
                it.overview,
                it.originalLanguage,
                it.releaseDate,
                it.popularity,
                it.voteAverage,
                it.id,
                it.title,
                it.voteCount,
                it.posterPath,
                favorite = it.favorite,
                isTvShows = it.isTvShows
            )
        }
    }

    fun mapDomainToEntity(input: Content): ContentEntity {
        return ContentEntity(
            input.overview,
            input.originalLanguage,
            input.releaseDate,
            input.popularity,
            input.voteAverage,
            input.id,
            input.title,
            input.voteCount,
            input.posterPath,
            favorite = input.favorite,
            isTvShows = input.isTvShows
        )
    }
}