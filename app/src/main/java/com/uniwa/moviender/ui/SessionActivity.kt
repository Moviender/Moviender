package com.uniwa.moviender.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.navArgs
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.ActivitySessionBinding
import com.uniwa.moviender.ui.viewmodel.SessionActivityViewModel

class SessionActivity : AppCompatActivity() {

    private val args: SessionActivityArgs by navArgs()
    private lateinit var binding: ActivitySessionBinding
    private val sharedViewModel: SessionActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_session, null, false)
        setContentView(binding.root)

        setStartDestination(args.type)
    }

    private fun setStartDestination(type: Int) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.session_nav_host)
        val inflater = navHostFragment?.findNavController()?.navInflater
        val graph = inflater?.inflate(R.navigation.session_navigation)
        when (type) {
            0 -> {
                sharedViewModel.setSessionId(args.sessionId!!)
                graph?.setStartDestination(R.id.votingFragment)
            }
            1 -> {
                sharedViewModel.setFriendUid(args.friendUid!!)
                graph?.setStartDestination(R.id.sessionGenresFragment)
            }
            2 -> {
                sharedViewModel.setSessionId(args.sessionId!!)
                sharedViewModel.setSessionStatus(type)
                graph?.setStartDestination(R.id.moviesSessionFragment)
            }
        }

        val navController = navHostFragment?.findNavController()
        navController?.graph = graph!!
    }
}