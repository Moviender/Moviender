package com.uniwa.moviender.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.FragmentMoviesBinding
import com.uniwa.moviender.model.MoviesViewModelFactory
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.MovienderApiService
import com.uniwa.moviender.ui.MoviesGridAdapter
import com.uniwa.moviender.ui.MoviesHorizontalAdapter
import com.uniwa.moviender.ui.MoviesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        adapter = MoviesGridAdapter(requireContext(), viewModel)

        binding?.apply {
            moviesFragment = this@MoviesFragment
            lifecycleOwner = viewLifecycleOwner
            hubMoviesGrid.adapter = adapter
        }

        adapter.submitList(listOf(16, 878, 37, 28, 53))
    }

}