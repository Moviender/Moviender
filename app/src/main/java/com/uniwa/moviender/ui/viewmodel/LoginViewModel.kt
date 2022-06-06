package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.uniwa.moviender.data.Error
import com.uniwa.moviender.data.placeholdersIndexes
import com.uniwa.moviender.model.User
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.apiservice.response.ResponseWrapper
import kotlinx.coroutines.launch
import java.net.ConnectException

class LoginViewModel : ViewModel() {
    private lateinit var user: FirebaseUser

    private val _isInitialized = MutableLiveData<Boolean>()
    val isInitialized: LiveData<Boolean> = _isInitialized

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> = _error

    private val _userInserted = MutableLiveData<Boolean>()
    val userInserted: LiveData<Boolean> = _userInserted

    fun setUser(user: FirebaseUser) {
        this@LoginViewModel.user = user
    }

    fun insertUser() {
        val profilePic =
            if (user.photoUrl != null) user.photoUrl.toString() else placeholdersIndexes.shuffled()[0]
        viewModelScope.launch {
            val response = MovienderApi.userClient.insertUser(
                User(
                    user.uid,
                    user.displayName!!,
                    profilePic
                )
            )

            if (isUserCreatedInRemoteDb(response)) {
                _userInserted.value = true
            }
            else {
                user.delete()
                _error.value = Error.USER_NOT_CREATED.code
            }
        }
    }

    fun isUserInitialized() {
        viewModelScope.launch {
            val response = MovienderApi.userClient.isInitialized(user.uid)

            if (response.failed) {
                _error.value = when (response.exception) {
                    is ConnectException -> Error.CANNOT_CONNECT.code
                    else -> Error.NETWORK_ERROR.code
                }
            } else {
                _isInitialized.value = response.body
            }
        }
    }

    fun getUid(): String = user.uid

    private fun isUserCreatedInRemoteDb(response: ResponseWrapper<Boolean>): Boolean =
        response.isSuccessful
}