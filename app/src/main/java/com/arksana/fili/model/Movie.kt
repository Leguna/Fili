package com.arksana.fili.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieList(
    @SerializedName("dates")
    @Expose
    val dates: Dates = Dates(),
    @SerializedName("page")
    @Expose
    val page: Int = 0,
    @SerializedName("results")
    @Expose
    val results: List<Movie> = listOf(),
    @SerializedName("total_pages")
    @Expose
    val totalPages: Int = 0,
    @SerializedName("total_results")
    @Expose
    val totalResults: Int = 0
)

data class Dates(
    @SerializedName("maximum")
    @Expose
    val maximum: String = "",
    @SerializedName("minimum")
    @Expose
    val minimum: String = ""
)

data class Movie(
    @SerializedName("adult")
    @Expose
    val adult: Boolean = false,
    @SerializedName("backdrop_path")
    @Expose
    val backdropPath: String = "",
    @SerializedName("genre_ids")
    @Expose
    val genreIds: List<Int> = listOf(),
    @SerializedName("id")
    @Expose
    val id: Int = 0,
    @SerializedName("original_language")
    @Expose
    val originalLanguage: String = "",
    @SerializedName("original_title")
    @Expose
    val originalTitle: String = "",
    @SerializedName("overview")
    @Expose
    val overview: String = "",
    @SerializedName("popularity")
    @Expose
    val popularity: Double = 0.0,
    @SerializedName("poster_path")
    @Expose
    val posterPath: String = "",
    @SerializedName("release_date")
    @Expose
    val releaseDate: String = "",
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("video")
    @Expose
    val video: Boolean = false,
    @SerializedName("vote_average")
    @Expose
    val voteAverage: Double = 0.0,
    @SerializedName("vote_count")
    @Expose
    val voteCount: Int = 0
)