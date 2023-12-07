package com.arksana.fili.ui.home

import androidx.lifecycle.LiveData
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
class HomeViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

    fun getPopularMovie(): LiveData<PagingData<Movie>> {
        return repository.getMovie().liveData.cachedIn(viewModelScope)
    }
}