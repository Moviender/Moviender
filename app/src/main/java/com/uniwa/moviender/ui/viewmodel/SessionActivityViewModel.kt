package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SessionActivityViewModel : ViewModel() {
    private lateinit var friendUid: String
    private lateinit var sessionId: String
    private var sessionStatus: Int = 0

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

    fun setSessionStatus(sessionStatus: Int) {
        this.sessionStatus = sessionStatus
    }

    fun getSessionStatus(): Int = sessionStatus
}