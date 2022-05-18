package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.uniwa.moviender.data.genres

class SessionGenresViewModel : ViewModel() {
    val genresList = genres.values.toList()
}