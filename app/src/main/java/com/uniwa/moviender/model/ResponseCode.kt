package com.uniwa.moviender.model

import com.squareup.moshi.Json

data class ResponseCode(
    @Json(name = "code") val code: Int
)