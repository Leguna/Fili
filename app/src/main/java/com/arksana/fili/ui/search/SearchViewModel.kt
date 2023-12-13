package com.arksana.fili.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.arksana.fili.model.Movie
import com.arksana.fili.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

    val query = MutableLiveData<String>()

    fun searchMovie(query: String): LiveData<PagingData<Movie>> {
        return repository.searchMovie(query).liveData.cachedIn(viewModelScope)
    }
}