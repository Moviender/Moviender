package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import com.uniwa.moviender.data.placeholdersIndexes
import com.uniwa.moviender.model.User
import com.uniwa.moviender.network.MovienderApi
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private lateinit var user: FirebaseUser

    private val _isInitialized = MutableLiveData<Boolean>()
    val isInitialized: LiveData<Boolean> = _isInitialized

    fun setUser(user: FirebaseUser) {
        this@LoginViewModel.user = user
    }

    fun insertUser() {
        val profilePic =
            if (user.photoUrl != null) user.photoUrl.toString() else placeholdersIndexes.shuffled()[0]
        viewModelScope.launch {
            MovienderApi.movienderApiService.insertUser(
                User(
                    user.uid,
                    user.displayName!!,
                    profilePic
                )
            )
        }
    }

    fun storeToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            viewModelScope.launch {
                MovienderApi.movienderApiService.storeToken(user.uid, token)
            }
        }
    }

    fun isUserInitialized() {
        viewModelScope.launch {
            _isInitialized.value = MovienderApi.movienderApiService.isInitialized(user.uid)
        }
    }

    fun getUid(): String = user.uid
}