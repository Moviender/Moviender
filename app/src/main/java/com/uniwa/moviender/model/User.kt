package com.uniwa.moviender.model

import com.squareup.moshi.Json

data class User(
    @Json(name = "uid") val uid: String,
    @Json(name = "username") val userName: String,
    @Json(name = "profile_pic_url") val profilePicUrl: String
)