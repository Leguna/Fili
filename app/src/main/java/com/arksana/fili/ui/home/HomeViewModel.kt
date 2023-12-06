package com.arksana.fili.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arksana.fili.model.MovieList
import com.arksana.fili.model.MovieList.*
import com.arksana.fili.repository.MovieApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val movieApiService: MovieApiService) :
    ViewModel() {

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>> = _movieList

    fun setMovieList(movieList: MovieList) {
        _movieList.value = movieList.results
    }

    fun getMovieList() = movieList.value

    fun refreshMovieList() {
        loadMovies()
    }

    fun loadMovies() {
        val call = movieApiService.getMovies()
        call.enqueue(object : Callback<MovieList> {
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if (response.isSuccessful) {
                    val movieList = response.body()
                    if (movieList != null) {
                        setMovieList(movieList)
                    }
                }
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
            }
        })
    }
}