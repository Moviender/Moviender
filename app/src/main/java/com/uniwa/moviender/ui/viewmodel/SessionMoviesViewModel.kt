package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.database.SessionDatabase
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.Rating
import com.uniwa.moviender.network.UserRatings
import kotlinx.coroutines.launch

class SessionMoviesViewModel(
    private val sessionId: String,
    private val uid: String,
    private val database: SessionDatabase
) : ViewModel() {
    private val _matchedMovies = MutableLiveData<List<Movie>>()
    val matchedMovies: LiveData<List<Movie>> = _matchedMovies

    fun getMovies() {
        viewModelScope.launch {
            val results = MovienderApi.movienderApiService.getSessionResult(sessionId)

            _matchedMovies.value = results.map { movielensId ->
                val movieDetails = MovienderApi.movienderApiService.getMovieDetails(movielensId, uid)

                Movie(
                    movielensId,
                    null,
                    movieDetails
                )
            }
        }
    }

    fun sendRating(movielensId: String, rating: Float) {
        viewModelScope.launch {
            MovienderApi.movienderApiService.updateRating(
                UserRatings(
                    uid,
                    listOf(Rating(movielensId, rating))
                )
            )
        }
    }
}