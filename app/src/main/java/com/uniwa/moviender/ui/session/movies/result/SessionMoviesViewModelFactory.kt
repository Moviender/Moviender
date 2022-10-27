package com.uniwa.moviender.ui.session.movies.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SessionMoviesViewModelFactory(
    private val sessionId: String,
    private val uid: String
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SessionMoviesViewModel(sessionId, uid) as T
    }
}