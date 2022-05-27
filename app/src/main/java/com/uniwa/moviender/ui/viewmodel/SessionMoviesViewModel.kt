package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.database.SessionDatabase
import com.uniwa.moviender.database.session.Movie
import com.uniwa.moviender.network.MovienderApi
import kotlinx.coroutines.launch

class SessionMoviesViewModel(
    private val sessionId: String,
    private val database: SessionDatabase
) : ViewModel() {
    private val _matchedMovies = MutableLiveData<List<Movie>>()
    val matchedMovies: LiveData<List<Movie>> = _matchedMovies

    fun getMovies() {
        viewModelScope.launch {
            val results =MovienderApi.movienderApiService.getSessionResult(sessionId)

                _matchedMovies.value = database.sessionDao().getResultMoviesDetails(results)


        }
    }
}