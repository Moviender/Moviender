package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.uniwa.moviender.network.MovienderApi
import kotlinx.coroutines.launch

class SessionActivityViewModel : ViewModel() {
    private lateinit var friendUid: String
    private lateinit var sessionId: String

    private val uid = FirebaseAuth.getInstance().uid!!

    fun setFriendUid(friendUid: String) {
        this.friendUid = friendUid
    }

    fun getFriendUid(): String = friendUid

    fun getUid(): String = uid

    fun setSessionId(sessionId: String) {
        this.sessionId = sessionId
    }

    fun getSessionId(): String = sessionId
}