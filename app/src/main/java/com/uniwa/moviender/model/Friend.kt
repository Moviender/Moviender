package com.uniwa.moviender.model

import com.squareup.moshi.Json

data class Friend(
    @Json(name = "uid") val uid: String,
    @Json(name = "username") val username: String,
    @Json(name = "state") val state: Int
) {
    enum class State(val state: Int) {
        PENDING(1),
        REQUEST(2),
        FRIEND(3)
    }

}

