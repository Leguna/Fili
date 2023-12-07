package com.arksana.fili.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.arksana.fili.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface MoviesRemoteDataSource {
    fun getMovies(): Flow<PagingData<Movie>>
}

internal class MoviesRemoteDataSourceImpl @Inject constructor(
    private val movieService: MovieApiService
) : MoviesRemoteDataSource {

    override fun getMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                MoviePagingSource(movieService)
            }
        ).flow
    }
}