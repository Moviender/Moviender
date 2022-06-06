package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.model.Friend
import com.uniwa.moviender.network.MovienderApi
import kotlinx.coroutines.launch

class FriendsFragmentViewModel : ViewModel() {


    private val _friendList = MutableLiveData<List<Friend>>()
    val friendList: LiveData<List<Friend>> = _friendList

    private val _isErrorEnabled = MutableLiveData<Boolean>()
    val isErrorEnabled: LiveData<Boolean> = _isErrorEnabled

    val friendUsername = MutableLiveData<String>()

    private val _requestResponse = MutableLiveData<Int>()
    val requestResponse: LiveData<Int> = _requestResponse

    private val _sessionId = MutableLiveData<String>()
    val sessionId: LiveData<String> = _sessionId

    private val _sessionState = MutableLiveData<Int>()
    val sessionState: LiveData<Int> = _sessionState

    private val _userState = MutableLiveData<Int>()
    val userState: LiveData<Int> = _userState

    private lateinit var friendUid: String

    private lateinit var uid: String

    fun setUid(uid: String) {
        this@FriendsFragmentViewModel.uid = uid
    }

    fun getFriends() {
        viewModelScope.launch {
            _friendList.value =
                MovienderApi.userClient.getFriendList(uid).body
        }
    }

    fun addFriend() {
        viewModelScope.launch {
            _requestResponse.value =
                MovienderApi.friendClient.friendRequest(uid, friendUsername.value!!).body
        }
    }

    fun respondToFriendRequest(friendUid: String, response: Int) {
        viewModelScope.launch {
            MovienderApi.friendClient.respondFriendRequest(uid, friendUid, response)
            getFriends()
        }

    }

    fun setError(isError: Boolean) {
        _isErrorEnabled.value = isError
    }

    fun setFriendUid(uid: String) {
        friendUid = uid
    }

    fun getFriendUid(): String = friendUid

    fun clearUserInput() {
        friendUsername.value = ""
    }

    fun deleteFriend(friend: Friend) {
        viewModelScope.launch {
            MovienderApi.friendClient.deleteFriend(uid, friend.uid)
            getFriends()
        }

    }

    fun closeSession() {
        viewModelScope.launch {
            _sessionId.value = MovienderApi.sessionClient.getSessionId(uid, friendUid).body
            MovienderApi.sessionClient.closeSession(_sessionId.value!!)
            getFriends()
        }
    }

    fun getSessionState() {
        viewModelScope.launch {
            _sessionId.value = MovienderApi.sessionClient.getSessionId(uid, friendUid).body
            _sessionState.value = MovienderApi.sessionClient.getSessionState(_sessionId.value!!).body
        }
    }

    fun getUserState() {
        viewModelScope.launch {
            _userState.value = MovienderApi.userClient.getUserState(_sessionId.value!!, uid).body
        }
    }
}