package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SessionActivityViewModel : ViewModel() {
    private lateinit var friendUid: String

    private val uid = FirebaseAuth.getInstance().uid!!

    fun setFriendUid(friendUid: String) {
        this.friendUid = friendUid
    }

    fun getFriendUid(): String = friendUid

    fun getUid(): String = uid
}