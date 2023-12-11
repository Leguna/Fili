package com.arksana.fili.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.arksana.fili.model.Movie
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val moviePagingSource: MoviePagingSource,
    private val movieSearchSource: MovieSearchSource
) {
    fun getMovie(): Pager<Int, Movie> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                moviePagingSource
            }
        )
    }

    fun searchMovie(query: String): Pager<Int, Movie> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                movieSearchSource.apply {
                    this.query = query
                }
            }
        )
    }
}
