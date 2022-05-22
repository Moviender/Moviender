package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.data.genres
import com.uniwa.moviender.data.nameToId
import com.uniwa.moviender.network.helper.SessionRequestBody
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.helper.SessionInitResponse
import kotlinx.coroutines.launch

class SessionGenresViewModel : ViewModel() {
    val genresList = genres.values.toList()
    val selectedGenres = mutableListOf<Int>()

    private val _response = MutableLiveData<SessionInitResponse>()
    val response: LiveData<SessionInitResponse> = _response

    fun onCheckedChanged(genre: Int, checked: Boolean) {
        if (checked) {
            selectedGenres.add(nameToId[genre]!!)
        } else {
            selectedGenres.remove(nameToId[genre]!!)
        }
    }

    fun startSession(uid: String, friendUid: String) {
        viewModelScope.launch {
            _response.value =
                MovienderApi.movienderApiService.initFriendsSession(
                    uid,
                    SessionRequestBody(friendUid, selectedGenres)
                )
        }
    }
}