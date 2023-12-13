package com.arksana.fili.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arksana.fili.model.Movie
import javax.inject.Inject

class MovieSearchSource @Inject constructor(
    private val service: MovieApiService,
    private val query: String,
) :
    PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val pageIndex = params.key ?: 1
        return try {
            val response = service.searchMovies(query = query, page = pageIndex)
            val movies = response.body()?.results ?: emptyList()
            val nextKey = if (movies.isEmpty()) null else pageIndex + (params.loadSize / 20)
            LoadResult.Page(
                data = movies,
                prevKey = if (pageIndex == 1) null else pageIndex,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}
