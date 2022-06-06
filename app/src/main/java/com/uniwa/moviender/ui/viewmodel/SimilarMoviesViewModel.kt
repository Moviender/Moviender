package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.helper.SessionInitResponse
import com.uniwa.moviender.network.helper.SessionRequestBodySim
import kotlinx.coroutines.launch

class SimilarMoviesViewModel : ViewModel() {
    private val _searchedResults = MutableLiveData<List<Movie>>()
    val searchedResults: LiveData<List<Movie>> = _searchedResults

    private val _response = MutableLiveData<SessionInitResponse>()
    val response: LiveData<SessionInitResponse> = _response

    fun searchByTitle(title: String) {
        viewModelScope.launch {
            _searchedResults.value = MovienderApi.movieClient.searchByTitle(title).body
        }
    }

    fun clearSearchResult() {
        _searchedResults.value = listOf()
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