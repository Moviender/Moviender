package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.uniwa.moviender.database.SessionDatabase
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.MovienderApiService
import com.uniwa.moviender.network.helper.UsersVotesBody
import com.uniwa.moviender.repository.SessionRepository
import com.uniwa.moviender.ui.adapters.VoteCardStackViewAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class VotingViewModel(
    private val uid: String,
    private val sessionId: String,
    private val database: SessionDatabase,
    movienderApi: MovienderApiService
) : ViewModel() {
    val movies = SessionRepository(uid, database, movienderApi).getSessionMovies(sessionId)

    private val _response = MutableLiveData<Int>()
    val response: LiveData<Int> = _response

    fun submitData(adapter: VoteCardStackViewAdapter) {
        viewModelScope.launch {
            movies.collectLatest { pageData ->
                adapter.submitData(pageData)
            }
        }
    }

    fun newVote(liked: Boolean) {
        viewModelScope.launch {
            database.withTransaction {
                database.sessionDao().insertVote(sessionId, liked)
            }
        }
    }

    fun sendVotes(uid: String){
        viewModelScope.launch {
            val votes = database.sessionDao().getVotes(sessionId)
            _response.value = MovienderApi.movienderApiService.sendVotes(sessionId, UsersVotesBody(uid, votes))
        }
    }
}