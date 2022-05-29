package com.uniwa.moviender.ui.fragment

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.ActivityNavigator
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.uniwa.moviender.R
import com.uniwa.moviender.ui.viewmodel.LoginViewModel

class FirebaseLoginFragment : Fragment() {

    private val sharedViewModel: LoginViewModel by activityViewModels()

    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    // See: https://developer.android.com/training/basics/intents/result
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_firebase_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set observer
        sharedViewModel.isInitialized.observe(viewLifecycleOwner) { initialized ->
            if (initialized) {
                findNavController().navigate(R.id.action_firebaseLoginFragment_to_hubActivity)
                ActivityNavigator(requireContext()).popBackStack()
            } else {
                sharedViewModel.setUser(firebaseUser!!)
                findNavController().navigate(R.id.action_firebaseLoginFragment_to_initializationFragment)
            }
        }

        if (isUserSignedIn()) {
            sharedViewModel.setUser(firebaseUser!!)
            checkInitialization()
        } else {
            createSignInIntent()
        }
    }

    private fun createSignInIntent() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )


        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .setTheme(R.style.Theme_Moviender)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse

        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (result.resultCode == RESULT_OK) {
            sharedViewModel.setUser(firebaseUser!!)
            if (response?.isNewUser == true) {
                sharedViewModel.insertUser()
                findNavController().navigate(R.id.action_firebaseLoginFragment_to_initializationFragment)
            } else {
                checkInitialization()
                sharedViewModel.storeToken()
            }
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

    private fun isUserSignedIn(): Boolean = firebaseUser != null

    private fun checkInitialization() {
        sharedViewModel.isUserInitialized()
    }
}