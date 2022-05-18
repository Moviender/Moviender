package com.uniwa.moviender.ui.viewmodel

import android.widget.CompoundButton
import androidx.lifecycle.ViewModel
import com.uniwa.moviender.data.genres
import com.uniwa.moviender.data.nameToId

class SessionGenresViewModel : ViewModel() {
    val genresList = genres.values.toList()
    val selectedGenres = mutableListOf<Int>()

    fun onCheckedChanged(genre: Int, checked: Boolean) {
        if (checked) {
            selectedGenres.add(nameToId[genre]!!)
        } else {
            selectedGenres.remove(nameToId[genre]!!)
        }
    }
}