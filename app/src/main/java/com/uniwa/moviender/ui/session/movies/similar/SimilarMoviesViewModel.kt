package com.uniwa.moviender.ui.session.movies.similar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.helper.SessionInitResponse
import com.uniwa.moviender.network.helper.SessionRequestBodySim
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SimilarMoviesViewModel : ViewModel() {
    private val _similarMovies = MutableSharedFlow<List<Movie>>()
    val similarMovies: SharedFlow<List<Movie>> = _similarMovies

    private val _response = MutableLiveData<SessionInitResponse>()
    val response: LiveData<SessionInitResponse> = _response

    fun searchByTitle(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            MovienderApi.movieClient.searchByTitle(title).let { response ->
                if (response.isSuccessful)
                    _similarMovies.emit(response.body)
            }
        }
    }

    fun clearSearchResult() {
        viewModelScope.launch(Dispatchers.Default) {
            _similarMovies.emit(listOf())
        }
    }

    fun startSession(uid: String, friendUid: String, movielensId: String) {
        viewModelScope.launch {
            _response.value =
                MovienderApi.sessionClient.initFriendsSessionSim(
                    uid,
                    SessionRequestBodySim(friendUid, movielensId)
                ).body
        }
    }
}