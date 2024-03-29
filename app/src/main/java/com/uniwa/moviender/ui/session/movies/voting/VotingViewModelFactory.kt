package com.uniwa.moviender.ui.session.movies.voting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uniwa.moviender.database.SessionDatabase

class VotingViewModelFactory(
    private val uid: String,
    private val sessionId: String,
    private val database: SessionDatabase,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VotingViewModel(uid, sessionId, database) as T
    }
}