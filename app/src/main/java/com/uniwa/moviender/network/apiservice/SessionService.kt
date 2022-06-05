package com.uniwa.moviender.network.apiservice

import com.uniwa.moviender.network.helper.SessionInitResponse
import com.uniwa.moviender.network.helper.SessionRequestBody
import com.uniwa.moviender.network.helper.SessionRequestBodySim
import com.uniwa.moviender.network.helper.UsersVotesBody
import retrofit2.http.*

interface SessionService {
    @GET("/session_id")
    suspend fun getSessionId(
        @Query("uid") uid: String,
        @Query("friend_uid") friendUid: String
    ): String

    @GET("/user_state/{session_id}")
    suspend fun getUserState(@Path("session_id") sessionId: String, @Query("uid") uid: String): Int

    @GET("/session_state/{session_id}")
    suspend fun getSessionState(@Path("session_id") sessionId: String): Int

    @GET("/session_user_votes/{session_id}")
    suspend fun getUserNumVotes(
        @Path("session_id") sessionId: String,
        @Query("uid") uid: String
    ): Int

    @GET("/session_results/{session_id}")
    suspend fun getSessionResult(@Path("session_id") sessionId: String): List<String>

    @Headers("Content-Type: application/json")
    @POST("/session_recommendations/{uid}")
    suspend fun initFriendsSession(
        @Path("uid") uid: String,
        @Body requestBody: SessionRequestBody
    ): SessionInitResponse

    @Headers("Content-Type: application/json")
    @POST("/session_sim/{uid}")
    suspend fun initFriendsSessionSim(
        @Path("uid") uid: String,
        @Body body: SessionRequestBodySim
    ): SessionInitResponse

    @POST("/vote_in_session/{session_id}")
    suspend fun sendVotes(
        @Path("session_id") sessionId: String,
        @Body votesBody: UsersVotesBody
    ): Int

    @POST("/close_session/{session_id}")
    suspend fun closeSession(@Path("session_id") sessionId: String): Boolean
}