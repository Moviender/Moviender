package com.uniwa.moviender.ui.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.UserRatings
import kotlinx.coroutines.launch

class StartupActivityViewModel : ViewModel() {
    private lateinit var uid: String
    private lateinit var ratings: UserRatings

    private lateinit var _friendUid: String
    var friendUid: String
        get() {
            return _friendUid
        }
        set(value) {
            _friendUid = value
        }

    private lateinit var _sessioId: String
    var sessionId: String
        get() {
            return _friendUid
        }
        set(value) {
            _friendUid = value
        }

    private val _bottomNavViewVisibility: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(View.GONE)
    }
    val bottomNavViewVisibility: LiveData<Int> = _bottomNavViewVisibility

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

    fun makeBottomNavViewVisible() {
        _bottomNavViewVisibility.value = View.VISIBLE
    }
}