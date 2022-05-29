package com.uniwa.moviender.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.uniwa.moviender.R
import com.uniwa.moviender.database.SessionDatabase
import com.uniwa.moviender.databinding.FragmentSessionMoviesBinding
import com.uniwa.moviender.ui.adapters.ResultMovieAdapter
import com.uniwa.moviender.ui.viewmodel.SessionActivityViewModel
import com.uniwa.moviender.ui.viewmodel.SessionMoviesViewModel
import com.uniwa.moviender.ui.viewmodel.SessionMoviesViewModelFactory

private const val DEFAULT_VALUE = 99

class SessionMoviesFragment : Fragment() {

    private val args: SessionMoviesFragmentArgs by navArgs()
    private lateinit var binding: FragmentSessionMoviesBinding
    private val sharedViewModel: SessionActivityViewModel by activityViewModels()
    private val viewModel: SessionMoviesViewModel by viewModels {
        SessionMoviesViewModelFactory(
            sharedViewModel.getSessionId(),
            sharedViewModel.getUid(),
            SessionDatabase.getInstance(requireContext())
        )
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

        val status = if (args.status == DEFAULT_VALUE) sharedViewModel.getSessionStatus() else args.status

        when (status) {
            1,2 -> showMatchedMovies()
        }
    }

    private fun showMatchedMovies() {
        val adapter = ResultMovieAdapter(this@SessionMoviesFragment)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            matchedMovies.adapter = adapter
        }

        viewModel.matchedMovies.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        viewModel.getMovies()
    }

    fun updateRating(rating: Float, ratingBar: RatingBar) {
        viewModel.sendRating(ratingBar.tag as String, rating)
    }

}