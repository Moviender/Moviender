package com.uniwa.moviender.ui.initialization

import android.widget.RatingBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.Rating
import kotlinx.coroutines.launch

class InitializationViewModel : ViewModel() {

    private val _ratedMovies = MutableLiveData<Int>(0)
    val ratedMovies: LiveData<Int> = _ratedMovies


    val REQUIRED_RATES: Int = 10

    private val ratings: HashMap<String, Float> = HashMap()

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    fun getStarterMovies() {
        viewModelScope.launch {
            _movies.value = MovienderApi.movieClient.getStarterMovies().body
        }
    }

    fun changeCounter(rating: Float, ratingBar: RatingBar) {
        val movilensId = ratingBar.tag as String
        if (ratings[movilensId] == 0f && rating > 0f) {
            _ratedMovies.value = _ratedMovies.value?.inc()
        } else if (ratings[movilensId]!! > 0f && rating == 0f) {
            _ratedMovies.value = _ratedMovies.value?.dec()
        }

        ratings[movilensId] = rating
    }

    fun addRating(movilensId: String) {
        // If simple assigned then when re-called from
        // the adapter the value would be back to 0
        ratings.getOrPut(movilensId) { 0f }
    }

    fun getRatings(): List<Rating> =
        ratings.filterValues { rating -> rating > 0 }
            .map { entry ->
                Rating(entry.key, entry.value)
            }
}