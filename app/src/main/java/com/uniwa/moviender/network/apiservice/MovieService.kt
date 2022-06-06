package com.uniwa.moviender.network.apiservice

import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.network.MovieDetails
import com.uniwa.moviender.network.UserRatings
import com.uniwa.moviender.network.helper.SessionMoviesPage
import retrofit2.Response
import retrofit2.http.*

interface MovieService : ApiService {
    @GET("/starter")
    suspend fun getStarterMovies(): Response<List<Movie>>

    @GET("/session_movies/{session_id}")
    suspend fun getSessionMovies(
        @Path("session_id") sessionId: String,
        @Query("uid") uid: String,
        @Query("next_page_key") nextPageKey: Int?
    ): Response<SessionMoviesPage>

    @GET("/movies/{page}")
    suspend fun getMovies(
        @Path("page") page: Int,
        @Query("genres") genres: List<Int>
    ): Response<List<Movie>>

    @GET("/movie_details/{movielens_id}")
    suspend fun getMovieDetails(
        @Path("movielens_id") movielensId: String,
        @Query("uid") uid: String
    ): Response<MovieDetails>

    @GET("/search")
    suspend fun searchByTitle(@Query("title") title: String): Response<List<Movie>>

    @GET("/user_recommendations/{page}")
    suspend fun getUserRecommendations(
        @Path("page") page: Int,
        @Query("uid") uid: String
    ): Response<List<Movie>>

    @Headers("Content-Type: application/json")
    @POST("/rating")
    suspend fun updateRating(@Body userRating: UserRatings): Response<Boolean>
}