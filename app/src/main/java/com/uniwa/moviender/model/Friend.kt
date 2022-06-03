package com.uniwa.moviender.model

import com.squareup.moshi.Json

data class Friend(
    @Json(name = "uid") val uid: String,
    @Json(name = "username") val username: String,
    @Json(name = "profile_pic_url") val profilePicUrl: String,
    @Json(name = "state") val state: Int
)

