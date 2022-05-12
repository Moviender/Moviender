package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uniwa.moviender.model.User

class FriendsFragmentViewModel : ViewModel() {

    private val _friendList = MutableLiveData<List<User>>()
    val friendList: LiveData<List<User>> = _friendList
}