package com.uniwa.moviender.ui.viewmodel

import android.widget.RatingBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.Rating
import com.uniwa.moviender.network.UserRatings
import kotlinx.coroutines.launch

class InitializationViewModel : ViewModel() {

    private val _ratedMovies = MutableLiveData<Int>(0)
    val ratedMovies: LiveData<Int> = _ratedMovies


    val REQUIRED_RATES: Int = 10

    private val oldRatings: HashMap<RatingBar, Float> = HashMap()
    private val movieRatingBar: HashMap<RatingBar, String> = HashMap()

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

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

    fun sendRatings(uid: String) {
        val ratings = oldRatings.filterValues { rating -> rating > 0 }
            .map { entry ->
                Rating(movieRatingBar[entry.key]!!, entry.value)
            }


        viewModelScope.launch {
            MovienderApi.movienderApiService.sendStarterRating(UserRatings(uid, ratings))
        }
    }
}