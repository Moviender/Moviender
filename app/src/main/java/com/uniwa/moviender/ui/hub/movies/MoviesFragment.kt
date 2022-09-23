package com.uniwa.moviender.ui.hub.movies

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.FragmentMoviesBinding
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.ui.StartupActivityViewModel

class MoviesFragment : Fragment() {

    private val SPAN_SEARCH = 3

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var adapter: MoviesGridAdapter
    private lateinit var searchAdapter: MoviesSearchAdapter
    private lateinit var moviesLayout: LinearLayoutManager
    private lateinit var searchLayout: GridLayoutManager
    private lateinit var callback: OnBackPressedCallback

    private val sharedViewModel: StartupActivityViewModel by activityViewModels()

    private val viewModel: MoviesViewModel by viewModels {
        MoviesViewModelFactory(
            MovienderApi.movieClient,
            listOf(-1, 28, 16, 80, 18, 27, 9648, 878, 37),
            sharedViewModel.getUid()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movies, container, false)

        binding.searchTv.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                viewModel.searchByTitle(text.toString())
            } else {
                viewModel.clearSearchResult()
            }
        }

        binding.moviesSearchToolbar.setOnMenuItemClickListener(object :
            Toolbar.OnMenuItemClickListener,
            androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(p0: MenuItem?): Boolean {
                return when (p0?.itemId) {
                    R.id.search_action -> {
                        prepareForSearch()
                        true
                    }
                    else -> false
                }
            }
        })

        callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            prepareForMovies()
        }
        callback.isEnabled = false

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MoviesGridAdapter(requireContext(), viewModel, this)
        searchAdapter = MoviesSearchAdapter(this)

        moviesLayout = LinearLayoutManager(requireContext())
        searchLayout = GridLayoutManager(requireContext(), SPAN_SEARCH)

        viewModel.changeLayoutManager(moviesLayout)

        binding.apply {
            moviesFragment = this@MoviesFragment
            lifecycleOwner = viewLifecycleOwner
            hubMoviesGrid.adapter = adapter

            viewModel = this@MoviesFragment.viewModel
        }

        adapter.submitList(viewModel.genres)
    }

    fun setSelectedMovie(movie: Movie) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        viewModel.setSelectedMovie(movie)
    }

    fun closeMovieDetailsFrame() {
        viewModel.changeFrameVisibility(View.GONE)
    }

    fun updateRating(rating: Float) {
        viewModel.sendRating(rating)
    }

    fun prepareForSearch() {
        binding.closeSearch.visibility = View.VISIBLE
        binding.searchTv.visibility = View.VISIBLE
        binding.moviesToolbarTitle.visibility = View.GONE
        binding.hubMoviesGrid.adapter = searchAdapter
        viewModel.changeLayoutManager(searchLayout)
        viewModel.searchedResults.observe(viewLifecycleOwner) { newList ->
            searchAdapter.submitList(newList) {
                searchLayout.scrollToPosition(0)
            }
        }
        callback.isEnabled = true
    }

    fun prepareForMovies() {
        binding.closeSearch.visibility = View.GONE
        binding.searchTv.visibility = View.GONE
        binding.moviesToolbarTitle.visibility = View.VISIBLE
        binding.hubMoviesGrid.adapter = adapter
        binding.searchTv.text.clear()
        viewModel.changeLayoutManager(moviesLayout)
        viewModel.clearSearchResult()
        callback.isEnabled = false

        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}