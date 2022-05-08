package com.uniwa.moviender.network

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class Movie(
    @Json(name = "movielens_id") val movielensId: String,
    @Json(name = "poster_path") val posterPath: String?,
    var movieDetails: MovieDetails?
)