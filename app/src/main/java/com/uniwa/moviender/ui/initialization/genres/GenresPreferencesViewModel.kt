package com.uniwa.moviender.ui.initialization.genres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.data.genres
import com.uniwa.moviender.data.nameToId
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.UserRatings
import com.uniwa.moviender.network.helper.UserGenrePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class GenresPreferencesViewModel : ViewModel() {
    private val _progress = MutableSharedFlow<Int>()
    val progress: SharedFlow<Int> = _progress

    val genresList = genres.values.toList()
    val selectedGenres = mutableListOf<Int>()

    fun addGenre(genre: Int) {
        selectedGenres.add(nameToId[genre]!!)
    }

    fun removeGenre(genre: Int) {
        selectedGenres.remove(nameToId[genre]!!)
    }

    fun sendInitializationData(ratings: UserRatings, uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            sendRatings(ratings)
            sendGenrePreferences(uid)
        }
    }

    private suspend fun sendRatings(ratings: UserRatings) {
        MovienderApi.userClient.sendStarterRating(ratings).let { response ->
            if (response.isSuccessful) _progress.emit(50)
        }
    }

    private suspend fun sendGenrePreferences(uid: String) {
        MovienderApi.userClient.sendGenrePreferences(
            UserGenrePreferences(
                uid,
                selectedGenres
            )
        ).let { response ->
            if (response.isSuccessful) _progress.emit(100)
        }
    }
}