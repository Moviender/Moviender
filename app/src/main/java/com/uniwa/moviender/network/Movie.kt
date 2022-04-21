package com.uniwa.moviender.network

import com.squareup.moshi.Json

data class Movie(
    @Json(name = "imdb_id") val imdb_id: String,
    @Json(name = "genre_ids") val genre_ids: Array<Int>,
    @Json(name = "poster_path") val poster_path: String?,
    @Json(name = "title") val title: String,
    @Json(name = "overview") val overview: String
)