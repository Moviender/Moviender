package com.uniwa.moviender.network

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class Movie(
    @Json(name = "movielens_id") val movielensId: String,
    @Json(name = "genre_ids") val genreIds: Array<Int>,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "title") val title: String,
    @Json(name = "overview") val overview: String,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "vote_average") val voteAverage: Double
)