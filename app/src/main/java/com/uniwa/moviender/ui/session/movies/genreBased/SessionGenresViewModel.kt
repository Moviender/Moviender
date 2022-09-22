package com.uniwa.moviender.ui.session.movies.genreBased

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.data.genres
import com.uniwa.moviender.data.nameToId
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.helper.SessionInitResponse
import com.uniwa.moviender.network.helper.SessionRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SessionGenresViewModel : ViewModel() {
    val genresList = genres.values.toList()

    private val _sessionId = MutableSharedFlow<SessionInitResponse>()
    val sessionId: SharedFlow<SessionInitResponse> = _sessionId

    private val selectedGenres = mutableListOf<Int>()

    fun addGenre(genre: Int) {
        selectedGenres.add(nameToId[genre]!!)
    }

    fun removeGenre(genre: Int) {
        selectedGenres.remove(nameToId[genre]!!)
    }

    fun startSession(uid: String, friendUid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            MovienderApi.sessionClient.initFriendsSession(
                uid,
                SessionRequestBody(friendUid, selectedGenres)
            ).let { response ->
                if (response.isSuccessful)
                    _sessionId.emit(response.body)
            }
        }
    }
}