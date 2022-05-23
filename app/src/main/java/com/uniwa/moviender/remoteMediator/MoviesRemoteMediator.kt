package com.uniwa.moviender.remoteMediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.uniwa.moviender.database.SessionDatabase
import com.uniwa.moviender.database.session.Movie
import com.uniwa.moviender.database.session.Session
import com.uniwa.moviender.database.session.SessionRemoteKeys
import com.uniwa.moviender.database.session.crossref.SessionMovieCrossRef
import com.uniwa.moviender.network.MovienderApiService
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val movienderApi: MovienderApiService,
    private val database: SessionDatabase,
    private val sessionId: String
) : RemoteMediator<Int, Movie>() {
    private val sessionDao = database.sessionDao()
    private val remoteKeysDao = database.remoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        val remoteKey = database.withTransaction {
            remoteKeysDao.getLastUpdated(sessionId)
        }
        if (remoteKey != null) {
            val lastUpdated = remoteKey.lastUpdated
            val cacheTimeOut = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
            return if (System.currentTimeMillis() - lastUpdated <= cacheTimeOut) {
                Log.d("session", "initialize: skip")
                InitializeAction.SKIP_INITIAL_REFRESH
            } else {
                Log.d("session", "initialize: launch")
                InitializeAction.LAUNCH_INITIAL_REFRESH
            }
        }
        else {
            return InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        remoteKeysDao.getRemoteKeyBySessionId(sessionId)
                    }

                    if (remoteKey.nextPageKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKey.nextPageKey
                }
            }

            val response = movienderApi.getSessionMovies(sessionId, loadKey)

            val movies = response.movies

            if (movies.isNotEmpty()) {
                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        sessionDao.deleteSession(sessionId)
                        // See deleteSession(sessionId) comments on SessionDao.kt
                        sessionDao.insertSession(Session(sessionId))
                    }

                    sessionDao.insertMovies(movies)

                    var orderIndex = sessionDao.getLastOrder(sessionId)

                    val crossRefs = movies.map { movie ->
                        SessionMovieCrossRef(sessionId, movie.movielensId, orderIndex++)
                    }

                    sessionDao.insertSessionMovieCrossRef(crossRefs)
                    remoteKeysDao.insertSessionRemoteKey(
                        SessionRemoteKeys(
                            sessionId,
                            response.nextPageKey
                        )
                    )
                }
            }

            MediatorResult.Success(endOfPaginationReached = response.nextPageKey == null || movies.isEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}