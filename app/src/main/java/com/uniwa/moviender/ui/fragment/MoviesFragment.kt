package com.uniwa.moviender.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.FragmentMoviesBinding
import com.uniwa.moviender.ui.viewmodel.MoviesViewModelFactory
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.ui.adapters.MoviesGridAdapter
import com.uniwa.moviender.ui.viewmodel.MoviesViewModel

class MoviesFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var adapter: MoviesGridAdapter

    private val viewModel: MoviesViewModel by viewModels {
        MoviesViewModelFactory(MovienderApi.movienderApiService, listOf(16, 878, 37, 28, 53))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MoviesGridAdapter(requireContext(), viewModel, this)

        binding.apply {
            moviesFragment = this@MoviesFragment
            lifecycleOwner = viewLifecycleOwner
            hubMoviesGrid.adapter = adapter
            viewModel = this@MoviesFragment.viewModel
        }

        adapter.submitList(viewModel.genres)
    }

    fun setSelectedMovie(movie: Movie) {
        viewModel.setSelectedMovie(movie)
    }

    fun closeMovieDetailsFrame() {
        viewModel.changeFrameVisibility(View.GONE)
    }

    fun updateRating(rating: Float, ratingBar: RatingBar) {
        viewModel.sendRating(rating)
    }

}