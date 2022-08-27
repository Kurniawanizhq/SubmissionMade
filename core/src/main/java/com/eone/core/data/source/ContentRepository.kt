package com.eone.core.data.source

import com.eone.core.data.source.local.LocalDataSource
import com.eone.core.data.source.remote.RemoteDataSource
import com.eone.core.data.source.remote.response.MovieResponse
import com.eone.core.data.source.remote.response.TvShowResponse
import com.eone.core.domain.model.Content
import com.eone.core.domain.repository.IContentRepository
import com.eone.core.utils.DataMapper
import com.eone.core.data.source.remote.network.ApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IContentRepository {

    override fun getAllMovies(sort: String): Flow<Resource<List<Content>>> =
        object : NetworkBoundResource<List<Content>, List<MovieResponse>>() {
            override fun loadFromDB(): Flow<List<Content>> {
                return localDataSource.getAllMovies(sort).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Content>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> {
                return remoteDataSource.getMovies()
            }

            override suspend fun saveCallResult(data: List<MovieResponse>) {
                val movieList = DataMapper.mapMovieResponsesToEntities(data)
                localDataSource.insertMovies(movieList)
            }
        }.asFlow()

    override fun getAllTvShows(sort: String): Flow<Resource<List<Content>>> =
        object : NetworkBoundResource<List<Content>, List<TvShowResponse>>() {
            override fun loadFromDB(): Flow<List<Content>> {
                return localDataSource.getAllTvShows(sort).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Content>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun createCall(): Flow<ApiResponse<List<TvShowResponse>>> {
                return remoteDataSource.getTvShows()
            }

            override suspend fun saveCallResult(data: List<TvShowResponse>) {
                val tvShowList = DataMapper.mapTvShowResponsesToEntities(data)
                localDataSource.insertMovies(tvShowList)
            }
        }.asFlow()



    override fun getSearchMovies(search: String): Flow<List<Content>> {
        return localDataSource.getMovieSearch(search).map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun getSearchTvShows(search: String): Flow<List<Content>> {
        return localDataSource.getTvShowSearch(search).map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun getFavoriteMovies(sort: String): Flow<List<Content>> {
        return localDataSource.getAllFavoriteMovies(sort).map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun getFavoriteTvShows(sort: String): Flow<List<Content>> {
        return localDataSource.getAllFavoriteTvShows(sort).map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun setMovieFavorite(content: Content, state: Boolean) {
        val movieEntity = DataMapper.mapDomainToEntity(content)
        CoroutineScope(Dispatchers.IO).launch {
            localDataSource.setMovieFavorite(movieEntity, state)
        }
    }
}