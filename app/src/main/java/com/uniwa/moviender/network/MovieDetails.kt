package com.uniwa.moviender.network

import com.squareup.moshi.Json

data class MovieDetails(
    @Json(name = "genre_ids") val genreIds: List<Int>,
    @Json(name = "title") val title: String,
    @Json(name = "overview") val overview: String,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "user_rating") val userRating: Float = 0f,
    @Json(name = "poster_path") val posterPath: String
)