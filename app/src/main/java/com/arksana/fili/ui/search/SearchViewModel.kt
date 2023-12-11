package com.arksana.fili.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.arksana.fili.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

    fun searchMovie(query: String) = repository.searchMovie(query).liveData.cachedIn(viewModelScope)
}