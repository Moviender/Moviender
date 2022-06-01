package com.uniwa.moviender.network.helper

import com.squareup.moshi.Json

data class SessionRequestBodySim(
    @Json(name = "friend_uid") val friendUid: String,
    @Json(name = "movielens_id") val movielensId: String
)