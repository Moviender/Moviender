package com.uniwa.moviender.network


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.uniwa.moviender.network.apiservice.FriendService
import com.uniwa.moviender.network.apiservice.MovieService
import com.uniwa.moviender.network.apiservice.SessionService
import com.uniwa.moviender.network.apiservice.UserService
import com.uniwa.moviender.network.client.FriendClient
import com.uniwa.moviender.network.client.MovieClient
import com.uniwa.moviender.network.client.SessionClient
import com.uniwa.moviender.network.client.UserClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

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

private val userService: UserService by lazy { retrofit.create(UserService::class.java) }
private val friendService: FriendService by lazy { retrofit.create(FriendService::class.java) }
private val movieService: MovieService by lazy { retrofit.create(MovieService::class.java) }
private val sessionService: SessionService by lazy { retrofit.create(SessionService::class.java) }

object MovienderApi {
    val userClient = UserClient(userService)
    val friendClient = FriendClient(friendService)
    val movieClient = MovieClient(movieService)
    val sessionClient = SessionClient(sessionService)
}