package com.uniwa.moviender.ui.session.movies.voting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.uniwa.moviender.R
import com.uniwa.moviender.data.SessionStatus
import com.uniwa.moviender.database.SessionDatabase
import com.uniwa.moviender.databinding.FragmentVotingBinding
import com.uniwa.moviender.listener.VotingButtonListener
import com.uniwa.moviender.listener.VotingCardListener
import com.uniwa.moviender.ui.StartupActivityViewModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.SwipeableMethod
import kotlinx.coroutines.flow.collectLatest

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

    private lateinit var votingMoviesAdapter: VoteCardStackViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        votingMoviesAdapter = VoteCardStackViewAdapter()
        val votingListener = VotingCardListener { liked ->
            viewModel.newVote(liked, votingMoviesAdapter.itemCount)
        }

        val manager = CardStackLayoutManager(requireContext(), votingListener).apply {
            setDirections(Direction.HORIZONTAL)
            setCanScrollHorizontal(true)
            setCanScrollVertical(false)
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        }

        binding.apply {
            votingStackView.adapter = votingMoviesAdapter
            votingStackView.layoutManager = manager
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@VotingFragment.viewModel

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
                this@VotingFragment.viewModel.sendVotes()
            }
        }

        observeMovies()
        viewModel.getVotes()
        setupObservers()
    }

    private fun setupObservers() {
        setupSessionStateObserver()
        setupUserStateObserver()
    }

    private fun setupSessionStateObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.sessionStatus.collectLatest { sessionStatus ->
                when (sessionStatus) {
                    SessionStatus.WAITING_FOR_VOTES.code -> {
                        viewModel.getUserState()
                    }
                    SessionStatus.SUCCESSFUL_FINISH.code,
                    SessionStatus.FAILED_FINISH.code -> {
                        val action =
                            VotingFragmentDirections.actionVotingFragmentToMoviesSessionFragment(
                                sessionStatus
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun setupUserStateObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.userState.collectLatest { userStatus ->
                val action =
                    VotingFragmentDirections.actionVotingFragmentToMoviesSessionFragment(userStatus)
                findNavController().navigate(action)
            }
        }
    }

    private fun observeMovies() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.movies.collectLatest { pageData ->
                votingMoviesAdapter.submitData(pageData)
            }
        }
    }

}