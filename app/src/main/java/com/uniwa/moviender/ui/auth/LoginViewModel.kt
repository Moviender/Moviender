package com.uniwa.moviender.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.uniwa.moviender.data.Error
import com.uniwa.moviender.data.placeholdersIndexes
import com.uniwa.moviender.model.User
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.apiservice.response.ResponseWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.net.ConnectException

class LoginViewModel : ViewModel() {
    private lateinit var user: FirebaseUser

    private val _isInitialized = MutableSharedFlow<Boolean>()
    val isInitialized: SharedFlow<Boolean> = _isInitialized

    private val _error = MutableSharedFlow<Int>()
    val error: SharedFlow<Int> = _error

    private val _userInserted = MutableSharedFlow<Boolean>()
    val userInserted: SharedFlow<Boolean> = _userInserted

    fun setUser(user: FirebaseUser) {
        this@LoginViewModel.user = user
    }

    fun insertUser() {
        val profilePic =
            if (user.photoUrl != null) user.photoUrl.toString() else placeholdersIndexes.shuffled()[0]
        viewModelScope.launch(Dispatchers.IO) {
            val response = MovienderApi.userClient.insertUser(
                User(
                    user.uid,
                    user.displayName!!,
                    profilePic
                )
            )

            if (isUserCreatedInRemoteDb(response)) {
                _userInserted.emit(true)
            } else {
                user.delete()
                _error.emit(Error.USER_NOT_CREATED.code)
            }
        }
    }

    fun isUserInitialized() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = MovienderApi.userClient.isInitialized(user.uid)

            if (response.failed) {
                val errorType = when (response.exception) {
                    is ConnectException -> Error.CANNOT_CONNECT.code
                    else -> Error.NETWORK_ERROR.code
                }

                _error.emit(errorType)
            } else {
                _isInitialized.emit(response.body)
            }
        }
    }

    fun getUid(): String = user.uid

    private fun isUserCreatedInRemoteDb(response: ResponseWrapper<Boolean>): Boolean =
        response.isSuccessful
}