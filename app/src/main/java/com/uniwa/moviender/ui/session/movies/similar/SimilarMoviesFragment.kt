package com.uniwa.moviender.ui.session.movies.similar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.FragmentSimilarMoviesBinding
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.ui.StartupActivityViewModel
import kotlinx.coroutines.flow.collectLatest

class SimilarMoviesFragment : Fragment() {

    private lateinit var binding: FragmentSimilarMoviesBinding
    private val viewModel: SimilarMoviesViewModel by viewModels()
    private val sharedViewModel: StartupActivityViewModel by activityViewModels()

    private lateinit var similarMoviesAdapter: SimilarMoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_similar_movies, null, false)

        binding.searchTv.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                viewModel.searchByTitle(text.toString())
            } else {
                viewModel.clearSearchResult()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        similarMoviesAdapter = SimilarMoviesAdapter(this@SimilarMoviesFragment)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            searchResultMovies.adapter = similarMoviesAdapter
            viewModel = this@SimilarMoviesFragment.viewModel
        }

        observeSimilarMovies()
    }

    fun startSession(view: View) {
        observeSessionStarted()

        val movie = view.tag as Movie
        viewModel.startSession(
            sharedViewModel.getUid(),
            sharedViewModel.friendUid,
            movie.movielensId
        )
    }

    private fun observeSimilarMovies() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.similarMovies.collectLatest { movies ->
                similarMoviesAdapter.submitList(movies) {
                    binding.searchResultMovies.layoutManager?.scrollToPosition(0)
                }
            }
        }
    }

    private fun observeSessionStarted() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.sessionId.collectLatest { response ->
                if (response.sessionId != null) {
                    sharedViewModel.sessionId = response.sessionId
                    findNavController().navigate(R.id.action_similarMoviesFragment_to_votingFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.session_already_exists),
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.action_similarMoviesFragment_to_hub_navigation)
                }
            }
        }
    }
}