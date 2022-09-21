package com.uniwa.moviender.ui.hub.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.model.Friend
import com.uniwa.moviender.network.MovienderApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class FriendsFragmentViewModel(private val uid: String) : ViewModel() {

    private val _friendList = MutableLiveData<List<Friend>>()
    val friendList: LiveData<List<Friend>> = _friendList

    private val _isErrorEnabled = MutableLiveData<Boolean>()
    val isErrorEnabled: LiveData<Boolean> = _isErrorEnabled

    val friendUsername = MutableLiveData<String>()

    private val _friendRequestStatus = MutableSharedFlow<Int>()
    val friendRequestStatus: SharedFlow<Int> = _friendRequestStatus

    private val _sessionState = MutableSharedFlow<Int>()
    val sessionState: SharedFlow<Int> = _sessionState

    private val _userState = MutableSharedFlow<Int>()
    val userState: SharedFlow<Int> = _userState

    private lateinit var friendUid: String

    lateinit var sessionId: String
        private set

    init {
        fetchFriends()
    }

    fun fetchFriends() {
        viewModelScope.launch(Dispatchers.IO) {
            _friendList.postValue(
                MovienderApi.userClient.getFriendList(uid).body
            )
        }
    }

    fun addFriend() {
        viewModelScope.launch(Dispatchers.IO) {
            MovienderApi.friendClient.friendRequest(uid, friendUsername.value!!).let { response ->
                if (response.isSuccessful)
                    _friendRequestStatus.emit(response.body)
            }
        }
    }

    fun respondToFriendRequest(friendUid: String, response: Int) {
        viewModelScope.launch {
            MovienderApi.friendClient.respondFriendRequest(uid, friendUid, response)
            fetchFriends()
        }

    }

    fun setError(isError: Boolean) {
        _isErrorEnabled.value = isError
    }

    fun setFriendUid(uid: String) {
        friendUid = uid
    }

    fun clearUserInput() {
        friendUsername.value = ""
    }

    fun deleteFriend(friend: Friend) {
        viewModelScope.launch {
            MovienderApi.friendClient.deleteFriend(uid, friend.uid)
            fetchFriends()
        }

    }

    fun closeSession() {
        viewModelScope.launch {
            sessionId = MovienderApi.sessionClient.getSessionId(uid, friendUid).body
            MovienderApi.sessionClient.closeSession(sessionId)
            fetchFriends()
        }
    }

    fun getSessionState() {
        viewModelScope.launch(Dispatchers.IO) {
            MovienderApi.sessionClient.getSessionId(uid, friendUid).let { response ->
                if (response.isSuccessful) {
                    sessionId = response.body
                    MovienderApi.sessionClient.getSessionState(sessionId)
                        .let { sessionStateResponse ->
                            if (response.isSuccessful)
                                _sessionState.emit(sessionStateResponse.body)
                        }
                }
            }
        }
    }

    fun getUserState() {
        viewModelScope.launch {
            _userState.emit(MovienderApi.userClient.getUserState(sessionId, uid).body)
        }
    }
}