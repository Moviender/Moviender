package com.uniwa.moviender.ui.hub.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FriendsViewModelFactory(private val uid: String) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FriendsFragmentViewModel(uid) as T
    }
}