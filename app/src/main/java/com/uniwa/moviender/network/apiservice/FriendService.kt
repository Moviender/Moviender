package com.uniwa.moviender.network.apiservice

import com.uniwa.moviender.network.apiservice.response.ResponseWrapper
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FriendService : ApiService {
    @POST("/friend_request/{uid}")
    suspend fun friendRequest(
        @Path("uid") uid: String,
        @Query("friend_username") friendUsername: String
    ): ResponseWrapper<Int>

    @POST("/respond_friend_request/{uid}")
    suspend fun respondFriendRequest(
        @Path("uid") uid: String,
        @Query("friend_uid") friendUid: String,
        @Query("response") response: Int
    ): ResponseWrapper<Boolean>

    @POST("/delete_friend/{uid}")
    suspend fun deleteFriend(
        @Path("uid") uid: String,
        @Query("friend_uid") friendUid: String
    ): ResponseWrapper<Boolean>
}