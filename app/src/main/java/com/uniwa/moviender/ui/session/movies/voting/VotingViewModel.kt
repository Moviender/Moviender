package com.uniwa.moviender.ui.session.movies.voting

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import com.uniwa.moviender.database.SessionDatabase
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.helper.UsersVotesBody
import com.uniwa.moviender.repository.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class VotingViewModel(
    private val uid: String,
    private val sessionId: String,
    private val database: SessionDatabase
) : ViewModel() {
    val MINIMUM_VOTES = 10

    private val _sendBtnVisibility = MutableLiveData(View.GONE)
    val sendBtnVisibility: LiveData<Int> = _sendBtnVisibility

    private var votesCount = 0

    val movies =
        SessionRepository(uid, database, MovienderApi.movieClient).getSessionMovies(sessionId)

    private val _sessionStatus = MutableSharedFlow<Int>()
    val sessionStatus: SharedFlow<Int> = _sessionStatus

    private val _userState = MutableSharedFlow<Int>()
    val userState: SharedFlow<Int> = _userState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val remoteDbVotesCount =
                MovienderApi.sessionClient.getUserNumVotes(sessionId, uid).body
            val localDbVotesCount = database.sessionDao().getVotes(sessionId).size

            votesCount = remoteDbVotesCount.coerceAtLeast(localDbVotesCount)

            changeVisibility()
        }
    }

    fun newVote(liked: Boolean, itemCount: Int) {
        viewModelScope.launch {
            database.withTransaction {
                database.sessionDao().insertVote(sessionId, liked)
            }
        }
        votesCount++
        changeVisibility()

        if (itemCount == 1) {
            sendVotes()
        }
    }

    fun sendVotes() {
        viewModelScope.launch {
            val votes = database.withTransaction {
                database.sessionDao().getVotes(sessionId)
            }
            _sessionStatus.emit(
                MovienderApi.sessionClient.sendVotes(sessionId, UsersVotesBody(uid, votes)).body
            )
        }
    }

    fun getUserState() {
        viewModelScope.launch {
            _userState.emit(MovienderApi.userClient.getUserState(sessionId, uid).body)
        }
    }

    private fun changeVisibility() {
        if (votesCount >= MINIMUM_VOTES && _sendBtnVisibility.value != View.VISIBLE) {
            _sendBtnVisibility.postValue(View.VISIBLE)
        }
    }
}