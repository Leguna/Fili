package com.arksana.fili.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.arksana.fili.model.Movie
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

interface MoviesRemoteDataSource {
    fun getMovies(): Pager<Int, Movie>
    fun searchMovies(query: String): Pager<Int, Movie>
}

class MoviesRemoteDataSourceImpl @Inject constructor(
    private val movieService: MovieApiService,
) : MoviesRemoteDataSource {

    override fun getMovies(): Pager<Int, Movie> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource(movieService)
            }
        )
    }

    override fun searchMovies(query: String): Pager<Int, Movie> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MovieSearchSource(movieService, query)
            }
        )
    }
}