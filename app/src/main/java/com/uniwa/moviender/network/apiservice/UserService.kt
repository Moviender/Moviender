package com.uniwa.moviender.network.apiservice

import com.uniwa.moviender.model.Friend
import com.uniwa.moviender.network.UserRatings
import com.uniwa.moviender.network.helper.UserGenrePreferences
import retrofit2.http.*

interface UserService {
    @GET("/initialized/{uid}")
    suspend fun isInitialized(@Path("uid") uid: String): Boolean

    @GET("/friends/{uid}")
    suspend fun getFriendList(@Path("uid") uid: String): List<Friend>

    @GET("/user_state/{session_id}")
    suspend fun getUserState(@Path("session_id") sessionId: String, @Query("uid") uid: String): Int

    @POST("/fcm_token/{uid}")
    suspend fun storeToken(@Path("uid") uid: String, @Query("token") token: String)

    @Headers("Content-Type: application/json")
    @POST("/userInitialization")
    suspend fun sendStarterRating(@Body userRatings: UserRatings)

    @POST("/userGenrePreference/")
    suspend fun sendGenrePreferences(@Body genrePreferences: UserGenrePreferences)
}