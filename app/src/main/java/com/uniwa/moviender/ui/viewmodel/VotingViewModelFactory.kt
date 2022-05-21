package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uniwa.moviender.database.SessionDatabase
import com.uniwa.moviender.network.MovienderApiService

class VotingViewModelFactory(
    private val sessionId: String,
    private val database: SessionDatabase,
    private val movienderApi: MovienderApiService
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VotingViewModel(sessionId, database, movienderApi) as T
    }
}