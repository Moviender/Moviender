package com.uniwa.moviender.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.uniwa.moviender.database.SessionDatabase
import com.uniwa.moviender.network.MovienderApiService
import com.uniwa.moviender.remoteMediator.MoviesRemoteMediator

class SessionRepository(
    private val uid: String,
    private val database: SessionDatabase,
    private val movienderApi: MovienderApiService
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getSessionMovies(sessionId: String) = Pager(
        config = PagingConfig(10),
        remoteMediator = MoviesRemoteMediator(uid, movienderApi, database, sessionId),
    ) {
        database.sessionDao().getSessionMovies(sessionId)
    }.flow
}