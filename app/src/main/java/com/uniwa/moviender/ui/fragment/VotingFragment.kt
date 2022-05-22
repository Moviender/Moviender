package com.uniwa.moviender.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.uniwa.moviender.R
import com.uniwa.moviender.database.SessionDatabase
import com.uniwa.moviender.databinding.FragmentVotingBinding
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.MovienderApiService
import com.uniwa.moviender.ui.adapters.VoteCardStackViewAdapter
import com.uniwa.moviender.ui.viewmodel.SessionActivityViewModel
import com.uniwa.moviender.ui.viewmodel.VotingViewModel
import com.uniwa.moviender.ui.viewmodel.VotingViewModelFactory

class VotingFragment : Fragment() {

    private lateinit var binding: FragmentVotingBinding
    private val sharedViewModel: SessionActivityViewModel by activityViewModels()
    private val viewModel: VotingViewModel by viewModels {
        VotingViewModelFactory(
            sharedViewModel.getSessionId(),
            SessionDatabase.getInstance(requireContext()),
            MovienderApi.movienderApiService
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = VoteCardStackViewAdapter()

        binding.apply {
            votingStackView.adapter = adapter
        }

        viewModel.submitData(adapter)
    }

}