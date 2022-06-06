package com.uniwa.moviender.network.apiservice

import com.uniwa.moviender.model.Friend
import com.uniwa.moviender.model.User
import com.uniwa.moviender.network.UserRatings
import com.uniwa.moviender.network.helper.UserGenrePreferences
import retrofit2.Response
import retrofit2.http.*

interface UserService : ApiService {
    @GET("/initialized/{uid}")
    suspend fun isInitialized(@Path("uid") uid: String): Response<Boolean>

    @GET("/friends/{uid}")
    suspend fun getFriendList(@Path("uid") uid: String): Response<List<Friend>>

    @GET("/user_state/{session_id}")
    suspend fun getUserState(
        @Path("session_id") sessionId: String,
        @Query("uid") uid: String
    ): Response<Int>

    @POST("/fcm_token/{uid}")
    suspend fun storeToken(
        @Path("uid") uid: String,
        @Query("token") token: String
    ): Response<Boolean>

    @POST("/user")
    suspend fun insertUser(@Body user: User): Response<Boolean>

    @Headers("Content-Type: application/json")
    @POST("/userInitialization")
    suspend fun sendStarterRating(@Body userRatings: UserRatings): Response<Boolean>

    @POST("/userGenrePreference/")
    suspend fun sendGenrePreferences(@Body genrePreferences: UserGenrePreferences): Response<Boolean>
}