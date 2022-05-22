package com.uniwa.moviender.network.helper

import com.squareup.moshi.Json

data class SessionInitResponse(
    @Json(name = "session_id") val sessionId: String?
)