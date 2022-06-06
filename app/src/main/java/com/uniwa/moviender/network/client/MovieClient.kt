package com.uniwa.moviender.network.client

import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.network.MovieDetails
import com.uniwa.moviender.network.UserRatings
import com.uniwa.moviender.network.apiservice.MovieService
import com.uniwa.moviender.network.apiservice.response.ResponseWrapper
import com.uniwa.moviender.network.helper.SessionMoviesPage

class MovieClient(
    private val service: MovieService
) : BaseClient() {
    suspend fun getStarterMovies(): ResponseWrapper<List<Movie>> {
        return safeApiCall { service.getStarterMovies() }
    }

    suspend fun getSessionMovies(
        sessionId: String,
        uid: String,
        nextPageKey: Int?
    ): ResponseWrapper<SessionMoviesPage> {
        return safeApiCall { service.getSessionMovies(sessionId, uid, nextPageKey) }
    }

    suspend fun getMovies(page: Int,genres: List<Int>): ResponseWrapper<List<Movie>> {
        return safeApiCall { service.getMovies(page, genres) }
    }

    suspend fun getMovieDetails(movielensId: String,uid: String): ResponseWrapper<MovieDetails> {
        return safeApiCall { service.getMovieDetails(movielensId, uid) }
    }

    suspend fun searchByTitle(title: String): ResponseWrapper<List<Movie>> {
        return safeApiCall { service.searchByTitle(title) }
    }

    suspend fun getUserRecommendations(page: Int, uid: String): ResponseWrapper<List<Movie>> {
        return safeApiCall { service.getUserRecommendations(page, uid) }
    }

    suspend fun updateRating(userRating: UserRatings): ResponseWrapper<Boolean> {
        return safeApiCall { service.updateRating(userRating) }
    }
}