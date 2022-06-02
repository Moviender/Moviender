package com.uniwa.moviender.network.helper

import com.squareup.moshi.Json

data class UserGenrePreferences (
    @Json(name = "uid") val uid: String,
    @Json(name = "genres_ids") val genreIds: List<Int>
)