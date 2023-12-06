package com.arksana.fili.repository

import com.arksana.fili.model.MovieList
import retrofit2.Call
import retrofit2.http.GET

interface MovieApiService {
    @GET("movie/now_playing")
    fun getMovies(): Call<MovieList>

}

