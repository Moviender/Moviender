package com.uniwa.moviender.network.apiservice

import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.network.MovieDetails
import com.uniwa.moviender.network.UserRatings
import com.uniwa.moviender.network.helper.SessionMoviesPage
import retrofit2.http.*

interface MovieService {
    @GET("/starter")
    suspend fun getStarterMovies(): List<Movie>

    @GET("/session_movies/{session_id}")
    suspend fun getSessionMovies(
        @Path("session_id") sessionId: String,
        @Query("uid") uid: String,
        @Query("next_page_key") nextPageKey: Int?
    ): SessionMoviesPage

    @GET("/movies/{page}")
    suspend fun getMovies(@Path("page") page: Int, @Query("genres") genres: List<Int>): List<Movie>

    @GET("/movie_details/{movielens_id}")
    suspend fun getMovieDetails(
        @Path("movielens_id") movielens_id: String,
        @Query("uid") uid: String
    ): MovieDetails

    @GET("/search")
    suspend fun searchByTitle(@Query("title") title: String): List<Movie>

    @GET("/user_recommendations/{page}")
    suspend fun getUserRecommendations(
        @Path("page") page: Int,
        @Query("uid") uid: String
    ): List<Movie>

    @Headers("Content-Type: application/json")
    @POST("/rating")
    suspend fun updateRating(@Body userRating: UserRatings)
}