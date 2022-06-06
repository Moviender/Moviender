package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.UserRatings
import kotlinx.coroutines.launch

class StartupActivityViewModel : ViewModel() {
    private lateinit var uid: String
    private lateinit var ratings: UserRatings

    fun setUid(uid: String) {
        this.uid = uid
    }

    fun getUid(): String = uid

    fun setUserRatings(ratings: UserRatings) {
        this.ratings = ratings
    }

    fun getUserRatings(): UserRatings = ratings

    fun storeToken(uid: String) {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            viewModelScope.launch {
                MovienderApi.userClient.storeToken(uid, token)
            }
        }
    }
}