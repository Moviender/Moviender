package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.database.SessionDatabase
import com.uniwa.moviender.network.MovienderApiService
import com.uniwa.moviender.repository.SessionRepository
import com.uniwa.moviender.ui.adapters.VoteCardStackViewAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class VotingViewModel(
    sessionId: String,
    database: SessionDatabase,
    movienderApi: MovienderApiService
) : ViewModel() {
    val movies = SessionRepository(database, movienderApi).getSessionMovies(sessionId)

    fun submitData(adapter: VoteCardStackViewAdapter) {
        viewModelScope.launch {
            movies.collectLatest { pageData ->
                adapter.submitData(pageData)
            }
        }
    }
}