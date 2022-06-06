package com.uniwa.moviender.network.client

import com.uniwa.moviender.network.apiservice.response.ResponseWrapper
import retrofit2.Response

open class BaseClient {
    inline fun <T> safeApiCall(apiCall: () -> Response<T>): ResponseWrapper<T> {
        return try {
            ResponseWrapper.success(apiCall.invoke())
        } catch (e: Exception) {
            ResponseWrapper.failure(e)
        }
    }
}