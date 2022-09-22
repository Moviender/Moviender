package com.uniwa.moviender.ui.session.movies.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.database.SessionDatabase
import com.uniwa.moviender.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SessionMoviesViewModel(
    private val sessionId: String,
    private val uid: String,
    private val database: SessionDatabase
) : ViewModel() {
    private val _matchedMovies = MutableLiveData<List<Movie>>()
    val matchedMovies: LiveData<List<Movie>> = _matchedMovies

    init {
        viewModelScope.launch(Dispatchers.IO) {
            MovienderApi.sessionClient.getSessionResult(sessionId).let { response ->
                if (response.isSuccessful) {
                    val matchedMoviesIds = response.body

                    _matchedMovies.value = matchedMoviesIds.map { movielensId ->
                        val movieDetails = getMovieDetails(movielensId)

                        Movie(
                            movielensId,
                            null,
                            movieDetails
                        )
                    }
                }
            }
        }
    }

    private suspend fun getMovieDetails(movielensId: String): MovieDetails? {
        MovienderApi.movieClient.getMovieDetails(movielensId, uid).let { response ->
            return if (response.isSuccessful)
                response.body
            else
                return null
        }
    }

    fun sendRating(movielensId: String, rating: Float) {
        viewModelScope.launch {
            MovienderApi.movieClient.updateRating(
                UserRatings(
                    uid,
                    listOf(Rating(movielensId, rating))
                )
            )
        }
    }
}