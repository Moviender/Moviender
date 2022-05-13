package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.model.Friend
import com.uniwa.moviender.model.ResponseCode
import com.uniwa.moviender.network.MovienderApi
import kotlinx.coroutines.launch

class FriendsFragmentViewModel : ViewModel() {

    private val _friendList = MutableLiveData<List<Friend>>()
    val friendList: LiveData<List<Friend>> = _friendList

    private val _isErrorEnabled = MutableLiveData<Boolean>()
    val isErrorEnabled: LiveData<Boolean> = _isErrorEnabled

    private val _errorText = MutableLiveData<String>()
    val errorText: LiveData<String> = _errorText

    val friendUsername = MutableLiveData<String>()

    private val _requestResponse = MutableLiveData<Int>()
    val requestResponse: LiveData<Int> = _requestResponse

    fun getFriends() {
        viewModelScope.launch {
            _friendList.value =
                MovienderApi.movienderApiService.getFriendList("123")
        }
    }

    fun addFriend() {
        viewModelScope.launch {
            _requestResponse.value =
                MovienderApi.movienderApiService.friendRequest("123", friendUsername.value!!)
        }
    }

    fun setError(isError: Boolean){
        _isErrorEnabled.value = isError
    }

    fun setErrorMessage(message: String){
        _errorText.value = message
    }
}