package com.arksana.fili.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arksana.fili.model.Movie
import com.arksana.fili.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(repository: MovieRepository) : ViewModel() {
    val allMovies: LiveData<List<Movie>> = repository.getAllFavoriteMovie()
}