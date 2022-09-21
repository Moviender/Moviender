package com.uniwa.moviender.ui.initialization.genres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.data.genres
import com.uniwa.moviender.data.nameToId
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.UserRatings
import com.uniwa.moviender.network.helper.UserGenrePreferences
import kotlinx.coroutines.launch

class GenresPreferencesViewModel : ViewModel() {
    val genresList = genres.values.toList()
    val selectedGenres = mutableListOf<Int>()

    fun addGenre(genre: Int) {
        selectedGenres.add(nameToId[genre]!!)
    }

    fun removeGenre(genre: Int) {
        selectedGenres.remove(nameToId[genre]!!)
    }

    fun sendRatings(ratings: UserRatings) {
        viewModelScope.launch {
            MovienderApi.userClient.sendStarterRating(ratings)
        }
    }

    fun sendGenrePreferences(uid: String) {
        viewModelScope.launch {
            MovienderApi.userClient.sendGenrePreferences(
                UserGenrePreferences(
                    uid,
                    selectedGenres
                )
            )
        }
    }
}