package com.uniwa.moviender.network.client

import com.uniwa.moviender.network.apiservice.FriendService
import com.uniwa.moviender.network.apiservice.response.ResponseWrapper

class FriendClient(
    private val service: FriendService
) : BaseClient() {
    suspend fun friendRequest(uid: String, friendUsername: String): ResponseWrapper<Int> {
        return safeApiCall { service.friendRequest(uid, friendUsername) }
    }

    suspend fun respondFriendRequest(
        uid: String,
        friendUid: String,
        response: Int
    ): ResponseWrapper<Boolean> {
        return safeApiCall { service.respondFriendRequest(uid, friendUid, response) }
    }

    suspend fun deleteFriend(uid: String, friendUid: String): ResponseWrapper<Boolean> {
        return safeApiCall { service.deleteFriend(uid, friendUid) }
    }
}