package com.uniwa.moviender.network

import com.squareup.moshi.Json

data class Rating (
    @Json(name = "movielens_id") val movielens_id: String,
    @Json(name = "rating") val rating: Float
)