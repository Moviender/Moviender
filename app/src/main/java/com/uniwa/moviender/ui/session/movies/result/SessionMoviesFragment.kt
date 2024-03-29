package com.uniwa.moviender.ui.session.movies.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.uniwa.moviender.R
import com.uniwa.moviender.data.RecommendationType
import com.uniwa.moviender.data.SessionStatus
import com.uniwa.moviender.data.SessionUserStatus
import com.uniwa.moviender.databinding.FragmentSessionMoviesBinding
import com.uniwa.moviender.ui.StartupActivityViewModel
import kotlinx.coroutines.flow.collectLatest

class SessionMoviesFragment : Fragment() {

    private val args: SessionMoviesFragmentArgs by navArgs()
    private lateinit var binding: FragmentSessionMoviesBinding
    private val sharedViewModel: StartupActivityViewModel by activityViewModels()
    private val viewModel: SessionMoviesViewModel by viewModels {
        SessionMoviesViewModelFactory(
            sharedViewModel.sessionId,
            sharedViewModel.getUid()
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
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@SessionMoviesFragment.viewModel
            matchedMovies.adapter = ResultMovieAdapter(this@SessionMoviesFragment)
            resultGroup.visibility = View.VISIBLE
        }

        observeRatingStored()
    }

    fun updateRating(rating: Float, ratingBar: RatingBar) {
        viewModel.storeRaring(ratingBar.tag as String, rating)
    }

    private fun showWaitingMessage() {
        binding.waitingTv.visibility = View.VISIBLE
    }

    private fun showVotingAgainDialog() {
        activity?.let {
            AlertDialog.Builder(it).apply {
                setMessage(getString(R.string.vote_again_msg))
                setPositiveButton(getString(R.string.vote_again_positive)) { _, _ ->
                    findNavController().navigate(R.id.action_moviesSessionFragment_to_votingFragment)
                }
                setNegativeButton(getString(R.string.vote_again_negative)) { _, _ ->
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
        when (status) {
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
        }?.let { destination ->
            findNavController().navigate(destination)
        }
    }

    private fun observeRatingStored() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.ratingStored.collectLatest { stored ->
                val text =
                    if (stored)
                        getString(R.string.rating_stored)
                    else
                        getString(R.string.rating_store_err)

                Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
            }
        }
    }

}