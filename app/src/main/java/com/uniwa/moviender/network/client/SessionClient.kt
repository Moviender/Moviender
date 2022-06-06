package com.uniwa.moviender.network.client

import com.uniwa.moviender.network.apiservice.SessionService
import com.uniwa.moviender.network.apiservice.response.ResponseWrapper
import com.uniwa.moviender.network.helper.SessionInitResponse
import com.uniwa.moviender.network.helper.SessionRequestBody
import com.uniwa.moviender.network.helper.SessionRequestBodySim
import com.uniwa.moviender.network.helper.UsersVotesBody

class SessionClient(
    private val service: SessionService
) : BaseClient() {
    suspend fun getSessionId(uid: String, friendUid: String): ResponseWrapper<String> {
        return safeApiCall { service.getSessionId(uid, friendUid) }
    }

    suspend fun getUserState(sessionId: String, uid: String): ResponseWrapper<Int> {
        return safeApiCall { service.getUserState(sessionId, uid) }
    }

    suspend fun getSessionState(sessionId: String): ResponseWrapper<Int> {
        return safeApiCall { service.getSessionState(sessionId) }
    }

    suspend fun getUserNumVotes(sessionId: String, uid: String): ResponseWrapper<Int> {
        return safeApiCall { service.getUserNumVotes(sessionId, uid) }
    }

    suspend fun getSessionResult(sessionId: String): ResponseWrapper<List<String>> {
        return safeApiCall { service.getSessionResult(sessionId) }
    }

    suspend fun initFriendsSession(
        uid: String,
        requestBody: SessionRequestBody
    ): ResponseWrapper<SessionInitResponse> {
        return safeApiCall { service.initFriendsSession(uid, requestBody) }
    }

    suspend fun initFriendsSessionSim(
        uid: String,
        body: SessionRequestBodySim
    ): ResponseWrapper<SessionInitResponse> {
        return safeApiCall { service.initFriendsSessionSim(uid, body) }
    }

    suspend fun sendVotes(sessionId: String, votesBody: UsersVotesBody): ResponseWrapper<Int> {
        return safeApiCall { service.sendVotes(sessionId, votesBody) }
    }

    suspend fun closeSession(sessionId: String): ResponseWrapper<Boolean> {
        return safeApiCall { service.closeSession(sessionId) }
    }

}