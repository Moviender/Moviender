package com.uniwa.moviender.network.client

import com.uniwa.moviender.model.Friend
import com.uniwa.moviender.model.User
import com.uniwa.moviender.network.UserRatings
import com.uniwa.moviender.network.apiservice.UserService
import com.uniwa.moviender.network.apiservice.response.ResponseWrapper
import com.uniwa.moviender.network.helper.UserGenrePreferences

class UserClient(
    private val service: UserService
) : BaseClient() {
    suspend fun isInitialized(uid: String): ResponseWrapper<Boolean> {
        return safeApiCall { service.isInitialized(uid) }
    }

    suspend fun getFriendList(uid: String): ResponseWrapper<List<Friend>> {
        return safeApiCall { service.getFriendList(uid) }
    }

    suspend fun getUserState(sessionId: String, uid: String): ResponseWrapper<Int> {
        return safeApiCall { service.getUserState(sessionId, uid) }
    }

    suspend fun storeToken(uid: String, token: String): ResponseWrapper<Boolean> {
        return safeApiCall { service.storeToken(uid, token) }
    }

    suspend fun insertUser(user: User): ResponseWrapper<Boolean> {
        return safeApiCall { service.insertUser(user) }
    }

    suspend fun sendStarterRating(userRatings: UserRatings): ResponseWrapper<Boolean> {
        return safeApiCall { service.sendStarterRating(userRatings) }
    }

    suspend fun sendGenrePreferences(genrePreferences: UserGenrePreferences): ResponseWrapper<Boolean> {
        return safeApiCall { service.sendGenrePreferences(genrePreferences) }
    }
}