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

    fun getFriends() {
        viewModelScope.launch {
            _friendList.value =
                MovienderApi.movienderApiService.getFriendList("123")
        }
    }
}