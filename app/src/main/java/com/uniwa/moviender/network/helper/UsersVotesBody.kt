package com.uniwa.moviender.network.helper

import com.squareup.moshi.Json

data class UsersVotesBody(
    @Json(name = "uid")
    val uid: String,
    @Json(name = "votes")
    val votes: List<Boolean>
)