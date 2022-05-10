package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uniwa.moviender.network.MovienderApiService

class MoviesViewModelFactory(
    private val service: MovienderApiService,
    private val genres: List<Int>
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoviesViewModel(service, genres) as T
    }
}