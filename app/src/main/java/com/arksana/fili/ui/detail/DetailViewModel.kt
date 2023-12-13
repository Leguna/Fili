package com.arksana.fili.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arksana.fili.model.Movie
import com.arksana.fili.repository.MovieRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    private val _movie = MutableLiveData<Movie>().apply {
        value = Movie()
    }
    val movie: LiveData<Movie> = _movie

    fun setMovie(args: String) {
        val movie = Gson().fromJson(args, Movie::class.java)
        _movie.value = movie
    }

    val allMovies: LiveData<List<Movie>> = repository.getAllFavoriteMovie()

    fun favoriteIconClicked() = viewModelScope.launch {
        val updatedMovie = movie.value
        val isFavorite = updatedMovie?.isFavorite ?: false
        if (!isFavorite) {
            repository.addFavoriteMovie(updatedMovie?.copy(isFavorite = true)!!)
        } else {
            repository.updateFavoriteMovie(updatedMovie?.copy(isFavorite = false)!!)
        }
    }

    fun setIsFavorite(favorite: Boolean) {
        val updatedMovie = movie.value
        updatedMovie?.isFavorite = favorite
        _movie.value = updatedMovie
    }
}