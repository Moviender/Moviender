package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.uniwa.moviender.network.UserRatings

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
}