package com.uniwa.moviender.ui.session.movies.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uniwa.moviender.database.SessionDatabase

class SessionMoviesViewModelFactory(
    private val sessionId: String,
    private val uid: String,
    private val database: SessionDatabase
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SessionMoviesViewModel(sessionId, uid, database) as T
    }
}