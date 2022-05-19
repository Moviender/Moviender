package com.uniwa.moviender.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
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

        sharedViewModel.setFriendUid(args.friendUid)

        navigate(args.type)
    }

    private fun navigate(type: Int) {
        when (type) {
            1 -> findNavController(R.id.session_nav_host).graph.setStartDestination(R.id.sessionGenresFragment)
        }
    }
}