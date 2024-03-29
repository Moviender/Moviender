package com.uniwa.moviender.ui.auth

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.uniwa.moviender.R
import com.uniwa.moviender.data.errorMessages
import com.uniwa.moviender.ui.StartupActivityViewModel
import kotlinx.coroutines.flow.collectLatest

class FirebaseLoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private val sharedViewModel: StartupActivityViewModel by activityViewModels()
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

        observeIsInitialized()
        observeError()

        if (isUserSignedIn()) {
            viewModel.setUser(firebaseUser!!)
            sharedViewModel.setUid(firebaseUser!!.uid)
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
            viewModel.setUser(firebaseUser!!)
            sharedViewModel.setUid(firebaseUser!!.uid)
            if (response?.isNewUser == true) {
                observeUserInserted()
                viewModel.insertUser()
            } else {
                checkInitialization()
            }
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

    private fun observeIsInitialized() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isInitialized.collectLatest { initialized ->
                if (initialized) {
                    sharedViewModel.storeToken(firebaseUser!!.uid)
                    findNavController().navigate(R.id.action_firebaseLoginFragment_to_hub_navigation)
                } else {
                    viewModel.setUser(firebaseUser!!)
                    sharedViewModel.setUid(firebaseUser!!.uid)
                    findNavController().navigate(R.id.action_firebaseLoginFragment_to_initializationFragment)
                }
            }
        }
    }

    private fun observeError() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest { errorType ->
                Toast.makeText(
                    requireContext(),
                    getString(errorMessages[errorType]!!),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun observeUserInserted() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.userInserted.collectLatest { inserted ->
                sharedViewModel.storeToken(firebaseUser!!.uid)
                findNavController().navigate(R.id.action_firebaseLoginFragment_to_initializationFragment)
            }
        }
    }

    private fun isUserSignedIn(): Boolean = firebaseUser != null

    private fun checkInitialization() {
        viewModel.isUserInitialized()
    }
}