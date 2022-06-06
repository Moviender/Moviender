package com.uniwa.moviender.network.apiservice

import com.uniwa.moviender.model.Friend
import com.uniwa.moviender.model.User
import com.uniwa.moviender.network.UserRatings
import com.uniwa.moviender.network.apiservice.response.ResponseWrapper
import com.uniwa.moviender.network.helper.UserGenrePreferences
import retrofit2.http.*

interface UserService : ApiService {
    @GET("/initialized/{uid}")
    suspend fun isInitialized(@Path("uid") uid: String): ResponseWrapper<Boolean>

    @GET("/friends/{uid}")
    suspend fun getFriendList(@Path("uid") uid: String): ResponseWrapper<List<Friend>>

    @GET("/user_state/{session_id}")
    suspend fun getUserState(
        @Path("session_id") sessionId: String,
        @Query("uid") uid: String
    ): ResponseWrapper<Int>

    @POST("/fcm_token/{uid}")
    suspend fun storeToken(
        @Path("uid") uid: String,
        @Query("token") token: String
    ): ResponseWrapper<Boolean>

    @POST("/user")
    suspend fun insertUser(@Body user: User): ResponseWrapper<Boolean>

    @Headers("Content-Type: application/json")
    @POST("/userInitialization")
    suspend fun sendStarterRating(@Body userRatings: UserRatings): ResponseWrapper<Boolean>

    @POST("/userGenrePreference/")
    suspend fun sendGenrePreferences(@Body genrePreferences: UserGenrePreferences): ResponseWrapper<Boolean>
}