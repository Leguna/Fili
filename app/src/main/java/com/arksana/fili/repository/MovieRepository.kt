package com.arksana.fili.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import com.arksana.fili.model.Movie
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource,
    private val movieDao: MovieDao,
) {
    fun getMovie(): Pager<Int, Movie> {
        return remoteDataSource.getMovies()
    }

    fun searchMovie(query: String): Pager<Int, Movie> {
        return remoteDataSource.searchMovies(query)
    }

    fun getAllFavoriteMovie(): LiveData<List<Movie>> {
        return movieDao.getAllFavoriteMovies()
    }

    suspend fun addFavoriteMovie(movie: Movie) {
        movieDao.insertMovie(movie)
    }

    suspend fun updateFavoriteMovie(movie: Movie) {
        movieDao.updateMovie(movie)
    }
}
