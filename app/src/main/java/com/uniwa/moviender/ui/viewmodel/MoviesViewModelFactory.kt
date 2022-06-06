package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uniwa.moviender.network.client.MovieClient

class MoviesViewModelFactory(
    private val service: MovieClient,
    private val genres: List<Int>,
    private val uid: String
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MoviesViewModel(service, genres, uid) as T
    }
}