package com.uniwa.moviender.network.helper

import com.squareup.moshi.Json

data class SessionRequestBody(
    @Json(name = "friend_uid") val friendUid: String,
    @Json(name = "genres_ids") val genresIds: List<Int>
)