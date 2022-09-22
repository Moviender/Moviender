package com.uniwa.moviender.ui.session.movies.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SessionMoviesViewModel(
    private val sessionId: String,
    private val uid: String
) : ViewModel() {
    private val _matchedMovies = MutableLiveData<List<Movie>>()
    val matchedMovies: LiveData<List<Movie>> = _matchedMovies

    private val _ratingStored = MutableSharedFlow<Boolean>()
    val ratingStored: SharedFlow<Boolean> = _ratingStored

    init {
        viewModelScope.launch(Dispatchers.IO) {
            MovienderApi.sessionClient.getSessionResult(sessionId).let { response ->
                if (response.isSuccessful) {
                    val matchedMoviesIds = response.body

                    matchedMoviesIds.map { movielensId ->
                        val movieDetails = getMovieDetails(movielensId)

                        Movie(
                            movielensId,
                            null,
                            movieDetails
                        )
                    }.also { _matchedMovies.postValue(it) }
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

    fun storeRaring(movielensId: String, rating: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            MovienderApi.movieClient.updateRating(
                UserRatings(
                    uid,
                    listOf(Rating(movielensId, rating))
                )
            ).let { response ->
                _ratingStored.emit(response.isSuccessful)
            }
        }
    }
}