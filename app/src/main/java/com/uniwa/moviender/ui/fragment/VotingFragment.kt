package com.uniwa.moviender.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.uniwa.moviender.R
import com.uniwa.moviender.database.SessionDatabase
import com.uniwa.moviender.databinding.FragmentVotingBinding
import com.uniwa.moviender.listener.VotingListener
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.ui.adapters.VoteCardStackViewAdapter
import com.uniwa.moviender.ui.viewmodel.SessionActivityViewModel
import com.uniwa.moviender.ui.viewmodel.VotingViewModel
import com.uniwa.moviender.ui.viewmodel.VotingViewModelFactory
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.SwipeableMethod

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

    private lateinit var votingListener: VotingListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        votingListener = VotingListener(viewModel)
        val adapter = VoteCardStackViewAdapter()

        val manager = CardStackLayoutManager(requireContext(), votingListener).apply {
            setDirections(Direction.HORIZONTAL)
            setCanScrollHorizontal(true)
            setCanScrollVertical(false)
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        }

        binding.apply {
            votingStackView.adapter = adapter
            votingStackView.layoutManager = manager
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.submitData(adapter)
    }

}