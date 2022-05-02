package com.uniwa.moviender.network

import com.squareup.moshi.Json

data class UserRatings (
    @Json(name = "uid") val uid: String,
    @Json(name = "ratings") val ratings: List<Rating>
    )