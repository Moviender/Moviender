package com.uniwa.moviender.ui.viewmodel

import android.content.Context
import android.widget.RatingBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import com.uniwa.moviender.data.UserDataStore
import com.uniwa.moviender.model.User
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.Rating
import com.uniwa.moviender.network.UserRatings
import kotlinx.coroutines.*

class LoginViewModel : ViewModel() {

    val REQUIRED_RATES: Int = 10

    private val oldRatings: HashMap<RatingBar, Float> = HashMap()
    private val movieRatingBar: HashMap<RatingBar, String> = HashMap()

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private lateinit var user: FirebaseUser

    private val _ratedMovies = MutableLiveData<Int>(0)
    val ratedMovies: LiveData<Int> = _ratedMovies

    private val _isInitialized = MutableLiveData<Boolean>()
    val isInitialized: LiveData<Boolean> = _isInitialized

    fun setUser(user: FirebaseUser) {
        this@LoginViewModel.user = user
    }

    fun insertUser() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            viewModelScope.launch {
                MovienderApi.movienderApiService.insertUser(User(user.uid, user.displayName!!))
                MovienderApi.movienderApiService.storeToken(user.uid, token)
            }
        }
    }

    fun storeToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            viewModelScope.launch {
                MovienderApi.movienderApiService.storeToken(user.uid, token)
            }
        }
    }

    fun getStarterMovies() {
        viewModelScope.launch {
            _movies.value = MovienderApi.movienderApiService.getStarterMovies()
        }
    }

    fun changeCounter(rating: Float, ratingBar: RatingBar) {
        // FIXME
        if (oldRatings[ratingBar] == 0f && rating > 0f) {
            _ratedMovies.value = _ratedMovies.value?.inc()
        } else if (oldRatings[ratingBar]!! > 0f && rating == 0f) {
            _ratedMovies.value = _ratedMovies.value?.dec()
        }

        oldRatings[ratingBar] = rating
    }

    fun addRatingBar(ratingBar: RatingBar) {
        // If simple assigned then when re-called from
        // the adapter the value would be back to 0
        oldRatings.getOrPut(ratingBar) { 0f }
    }

    fun addMovieRatingBar(ratingBar: RatingBar, movieId: String) {
        movieRatingBar.getOrPut(ratingBar) { movieId }
    }

    fun isUserInitialized() {
        viewModelScope.launch {
            _isInitialized.value = MovienderApi.movienderApiService.isInitialized(user.uid)
        }
    }

    fun sendRatings() {
        val ratings = oldRatings.filterValues { rating -> rating > 0 }
            .map { entry ->
                Rating(movieRatingBar[entry.key]!!, entry.value)
            }


        viewModelScope.launch {
            MovienderApi.movienderApiService.sendStarterRating(UserRatings(user.uid, ratings))
        }
    }

    fun saveUID(context: Context) {
        viewModelScope.launch {
            UserDataStore(context).saveUserUid(user.uid, context)
        }
    }
}