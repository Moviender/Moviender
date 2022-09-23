package com.uniwa.moviender.ui.session.movies.similar

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
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

        similarMoviesAdapter = SimilarMoviesAdapter { movie ->
            showSelectedMovie(movie)
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            searchResultMovies.adapter = similarMoviesAdapter
            similarMoviesFragment = this@SimilarMoviesFragment
            viewModel = this@SimilarMoviesFragment.viewModel
        }

        observeSimilarMovies()
        observeSessionStarted()
    }

    private fun showSelectedMovie(movie: Movie) {
        viewModel.setSelectedMovie(movie)
        binding.apply {
            selectedMoviePoster.show()
            submitSelection.makeVisible()
            searchResultMovies.animate().alpha(0.5f).duration = 370
        }
    }

    fun startSession() {
        binding.selectedMoviePoster.sendAnimation()
        binding.submitSelection.makeInvisible()

        viewModel.startSession(
            sharedViewModel.getUid(),
            sharedViewModel.friendUid,
            viewModel.selectedMovie.value!!.movielensId
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

    private fun FrameLayout.show() {
        scaleX = 0f
        scaleY = 0f
        visibility = View.VISIBLE
        animate().scaleXBy(1f).scaleYBy(1f).apply {
            duration = 300
            start()
        }
    }

    private fun FrameLayout.sendAnimation() {
        animate().translationYBy((-800f).dp).apply {
            duration = 230
            withEndAction {
                binding.similarMoviesProgress.show()
            }
        }
    }

    private fun ExtendedFloatingActionButton.makeVisible() {
        alpha = 0f
        visibility = View.VISIBLE
        animate().alpha(1f).apply {
            duration = 500
            start()
        }
    }

    private fun ExtendedFloatingActionButton.makeInvisible() {
        isEnabled = false

        animate().alpha(0f).apply {
            duration = 250
            withEndAction {
                visibility = View.GONE
            }
        }
    }

    private val Float.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(),
            resources.displayMetrics
        )
}