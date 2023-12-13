package com.arksana.fili.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "response_movie")
data class MovieList(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @SerializedName("dates")
    @Expose
    var dates: Dates = Dates(),
    @SerializedName("page")
    @Expose
    var page: Int = 0,
    @SerializedName("results")
    @Expose
    var results: List<Movie> = listOf(),
    @SerializedName("total_pages")
    @Expose
    var totalPages: Int = 0,
    @SerializedName("total_results")
    @Expose
    var totalResults: Int = 0,
)

data class Dates(
    @SerializedName("maximum")
    @Expose
    var maximum: String = "",
    @SerializedName("minimum")
    @Expose
    var minimum: String = "",
)

@Entity(tableName = "movies")
data class Movie(
    @SerializedName("is_favorite")
    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false,
    @SerializedName("adult")
    @Expose
    var adult: Boolean = false,
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String = "",
    @SerializedName("genre_ids")
    @Expose
    @Ignore
    var genreIds: List<Int> = listOf(),
    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @SerializedName("original_language")
    @Expose
    var originalLanguage: String = "",
    @SerializedName("original_title")
    @Expose
    @ColumnInfo(name = "original_title")
    var originalTitle: String = "",
    @SerializedName("overview")
    @Expose
    @ColumnInfo(name = "overview")
    var overview: String = "",
    @SerializedName("popularity")
    @Expose
    var popularity: Double = 0.0,
    @SerializedName("poster_path")
    @Expose
    var posterPath: String = "",
    @SerializedName("release_date")
    @Expose
    var releaseDate: String = "",
    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    var title: String = "",
    @SerializedName("video")
    @Expose
    var video: Boolean = false,
    @SerializedName("vote_average")
    @Expose
    var voteAverage: Double = 0.0,
    @SerializedName("vote_count")
    @Expose
    var voteCount: Int = 0,
)