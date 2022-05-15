package com.uniwa.moviender.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.uniwa.moviender.R
import com.uniwa.moviender.ui.HubActivity

class HubViewModel(
    navController: NavController
) : ViewModel() {
    private val auth = FirebaseAuth.getInstance().addAuthStateListener { auth ->
        if (!isUserSignedIn(auth.currentUser)) {
            navController.navigate(R.id.action_hubActivity_to_startupActivity)
        }
    }

    private val _uid = FirebaseAuth.getInstance().currentUser!!.uid
    val uid = _uid


    private fun isUserSignedIn(firebaseUser: FirebaseUser?): Boolean = firebaseUser != null
}