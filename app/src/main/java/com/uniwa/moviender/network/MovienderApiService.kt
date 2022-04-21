package com.uniwa.moviender.network


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.uniwa.moviender.model.User
import okhttp3.OkHttpClient
import org.json.JSONArray
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

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
}

object MovienderApi {
    val movienderApiService: MovienderApiService by lazy { retrofit.create(MovienderApiService::class.java)}
}