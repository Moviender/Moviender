package com.uniwa.moviender.network.helper

import com.squareup.moshi.Json
import com.uniwa.moviender.database.session.Movie

data class SessionMoviesPage(
    @Json(name = "movies") val movies: List<Movie>,
    @Json(name = "next_page_key") val nextPageKey: Int?
)