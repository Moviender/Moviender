package com.uniwa.moviender.model

import com.squareup.moshi.Json

data class User(
    @Json(name = "uid") val uid: String
)