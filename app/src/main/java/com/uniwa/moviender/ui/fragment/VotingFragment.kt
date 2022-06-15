package com.uniwa.moviender.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.uniwa.moviender.R
import com.uniwa.moviender.SessionNavigationDirections
import com.uniwa.moviender.database.SessionDatabase
import com.uniwa.moviender.databinding.FragmentVotingBinding
import com.uniwa.moviender.listener.VotingButtonListener
import com.uniwa.moviender.listener.VotingCardListener
import com.uniwa.moviender.ui.adapters.VoteCardStackViewAdapter
import com.uniwa.moviender.ui.viewmodel.StartupActivityViewModel
import com.uniwa.moviender.ui.viewmodel.VotingViewModel
import com.uniwa.moviender.ui.viewmodel.VotingViewModelFactory
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.SwipeableMethod

class VotingFragment : Fragment() {

    private lateinit var binding: FragmentVotingBinding
    private val sharedViewModel: StartupActivityViewModel by activityViewModels()
    private val viewModel: VotingViewModel by viewModels {
        VotingViewModelFactory(
            sharedViewModel.getUid(),
            sharedViewModel.sessionId,
            SessionDatabase.getInstance(requireContext())
        )
    }

    private lateinit var votingListener: VotingCardListener

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
        votingListener = VotingCardListener(viewModel, adapter)

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
            viewModel = this@VotingFragment.viewModel

            // Todo move them to xml
            prevMovieBtn.setOnClickListener(
                VotingButtonListener(
                    manager,
                    Direction.Left,
                    votingStackView
                )
            )
            nextMovieBtn.setOnClickListener(
                VotingButtonListener(
                    manager,
                    Direction.Right,
                    votingStackView
                )
            )
            saveMoviesBtn.setOnClickListener {
                sendVotes()
            }
        }

        viewModel.submitData(adapter)

        viewModel.getVotes()

        viewModel.sessionStatus.observe(requireActivity()) { status ->
            val action =
                SessionNavigationDirections.actionVotingFragmentToMoviesSessionFragment(status)
            findNavController().navigate(action)
        }
    }

    private fun sendVotes() {
        viewModel.sendVotes()
    }

}