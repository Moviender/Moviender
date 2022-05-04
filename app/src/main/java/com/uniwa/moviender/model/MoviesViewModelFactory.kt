package com.uniwa.moviender.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uniwa.moviender.network.MovienderApiService
import com.uniwa.moviender.ui.MoviesViewModel

class MoviesViewModelFactory(
    private val service: MovienderApiService,
    private val genres: List<Int>
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoviesViewModel(service, genres) as T
    }
}