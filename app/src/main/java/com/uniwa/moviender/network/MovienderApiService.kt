package com.uniwa.moviender.network


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.uniwa.moviender.model.Friend
import com.uniwa.moviender.model.User
import com.uniwa.moviender.network.helper.SessionInitResponse
import com.uniwa.moviender.network.helper.SessionMoviesPage
import com.uniwa.moviender.network.helper.SessionRequestBody
import com.uniwa.moviender.network.helper.UsersVotesBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "http://10.0.2.2:8000"

val client = OkHttpClient.Builder()
    .followSslRedirects(false)
    .followRedirects(false)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

interface MovienderApiService {
    @Headers("Content-Type: application/json")
    @POST("user")
    suspend fun insertUser(@Body user: User): String

    @GET("/starter")
    suspend fun getStarterMovies(): List<Movie>

    @GET("/movies/{page}")
    suspend fun getMovies(@Path("page") page: Int, @Query("genres") genres: List<Int>): List<Movie>

    @GET("movie_details/{movielens_id}")
    suspend fun getMovieDetails(
        @Path("movielens_id") movielens_id: String,
        @Query("uid") uid: String
    ): MovieDetails

    @Headers("Content-Type: application/json")
    @POST("/userInitialization")
    suspend fun sendStarterRating(@Body userRatings: UserRatings)

    @Headers("Content-Type: application/json")
    @POST("/rating")
    suspend fun updateRating(@Body userRating: UserRatings)

    @GET("/search")
    suspend fun searchByTitle(@Query("title") title: String): List<Movie>

    @POST("/friend_request/{uid}")
    suspend fun friendRequest(
        @Path("uid") uid: String,
        @Query("friend_username") friendUsername: String
    ): Int

    @POST("/respond_friend_request/{uid}")
    suspend fun respondFriendRequest(
        @Path("uid") uid: String,
        @Query("friend_uid") friendUid: String,
        @Query("response") response: Int
    )

    @GET("/friends/{uid}")
    suspend fun getFriendList(@Path("uid") uid: String): List<Friend>

    @POST("/delete_friend/{uid}")
    suspend fun deleteFriend(@Path("uid") uid: String, @Query("friend_uid") friendUid: String)

    @GET("/initialized/{uid}")
    suspend fun isInitialized(@Path("uid") uid: String): Boolean

    @POST("/fcm_token/{uid}")
    suspend fun storeToken(@Path("uid") uid: String, @Query("token") token: String)

    @Headers("Content-Type: application/json")
    @POST("/session/{uid}")
    suspend fun initFriendsSession(
        @Path("uid") uid: String,
        @Body requestBody: SessionRequestBody
    ): SessionInitResponse?

    @GET("/session_movies/{session_id}")
    suspend fun getSessionMovies(
        @Path("session_id") sessionId: String,
        @Query("uid") uid: String,
        @Query("next_page_key") nextPageKey: Int?
    ): SessionMoviesPage

    @GET("/session_id")
    suspend fun getSessionId(
        @Query("uid") uid: String,
        @Query("friend_uid") friendUid: String
    ): String

    @GET("/session_state/{session_id}")
    suspend fun getSessionState(@Path("session_id") sessionId: String): Int

    @GET("/user_state/{session_id}")
    suspend fun getUserState(@Path("session_id") sessionId: String, @Query("uid") uid: String): Int

    @POST("/vote_in_session/{session_id}")
    suspend fun sendVotes(
        @Path("session_id") sessionId: String,
        @Body votesBody: UsersVotesBody
    ): Int

    @GET("/session_results/{session_id}")
    suspend fun getSessionResult(@Path("session_id") sessionId: String): List<String>
}

object MovienderApi {
    val movienderApiService: MovienderApiService by lazy { retrofit.create(MovienderApiService::class.java) }
}