package com.uniwa.moviender.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.uniwa.moviender.R
import com.uniwa.moviender.data.RecommendationType
import com.uniwa.moviender.data.SessionStatus
import com.uniwa.moviender.data.SessionUserStatus
import com.uniwa.moviender.database.SessionDatabase
import com.uniwa.moviender.databinding.FragmentSessionMoviesBinding
import com.uniwa.moviender.ui.adapters.ResultMovieAdapter
import com.uniwa.moviender.ui.viewmodel.SessionMoviesViewModel
import com.uniwa.moviender.ui.viewmodel.SessionMoviesViewModelFactory
import com.uniwa.moviender.ui.viewmodel.StartupActivityViewModel

class SessionMoviesFragment : Fragment() {

    private val args: SessionMoviesFragmentArgs by navArgs()
    private lateinit var binding: FragmentSessionMoviesBinding
    private val sharedViewModel: StartupActivityViewModel by activityViewModels()
    private val viewModel: SessionMoviesViewModel by viewModels {
        SessionMoviesViewModelFactory(
            sharedViewModel.sessionId,
            sharedViewModel.getUid(),
            SessionDatabase.getInstance(requireContext())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        determineNavigation(args.status)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_session_movies, null, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (args.status) {
            SessionStatus.FAILED_FINISH.code -> showFailedMessage()
            SessionUserStatus.WAITING.code, SessionStatus.WAITING_FOR_VOTES.code -> showWaitingMessage()
            SessionStatus.SUCCESSFUL_FINISH.code -> showMatchedMovies()
            SessionUserStatus.VOTING_AGAIN.code -> showVotingAgainDialog()
        }
    }

    private fun showMatchedMovies() {
        val adapter = ResultMovieAdapter(this@SessionMoviesFragment)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            matchedMovies.adapter = adapter
            resultGroup.visibility = View.VISIBLE
        }

        viewModel.matchedMovies.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        viewModel.getMovies()
    }

    fun updateRating(rating: Float, ratingBar: RatingBar) {
        viewModel.sendRating(ratingBar.tag as String, rating)
    }

    private fun showWaitingMessage() {
        binding.waitingTv.visibility = View.VISIBLE
    }

    private fun showVotingAgainDialog() {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage(getString(R.string.vote_again_msg))
                setPositiveButton(getString(R.string.vote_again_positive)) { dialog, id ->
                    findNavController().navigate(R.id.action_moviesSessionFragment_to_votingFragment)
                }
                setNegativeButton(getString(R.string.vote_again_negative)) { dialog, id ->
                    findNavController().navigate(R.id.action_moviesSessionFragment_to_hub_navigation)
                }
            }
                .create()
        }?.show()
    }

    private fun showFailedMessage() {
        binding.failedTv.visibility = View.VISIBLE
    }

    private fun determineNavigation(status: Int) {
        val destination: Int? = when (status) {
            SessionUserStatus.VOTING.code -> {
                R.id.action_moviesSessionFragment_to_votingFragment
            }
            RecommendationType.SVD.code -> {
                R.id.action_moviesSessionFragment_to_sessionGenresFragment
            }
            RecommendationType.KNN.code -> {
                R.id.action_moviesSessionFragment_to_similarMoviesFragment
            }
            else -> {
                null
            }
        }

        destination?.let {
            findNavController().navigate(it)
        }
    }

}