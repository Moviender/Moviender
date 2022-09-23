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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.FragmentMoviesBinding
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.ui.StartupActivityViewModel
import kotlinx.coroutines.flow.collectLatest

private const val SPAN_SEARCH = 3

class MoviesFragment : Fragment() {

    private val sharedViewModel: StartupActivityViewModel by activityViewModels()

    private val viewModel: MoviesViewModel by viewModels {
        MoviesViewModelFactory(
            sharedViewModel.getUid()
        )
    }
    private lateinit var binding: FragmentMoviesBinding

    private lateinit var moviesAdapter: MoviesGenresAdapter
    private lateinit var searchAdapter: MoviesSearchAdapter
    private val moviesLayout = LinearLayoutManager(requireContext())
    private val searchLayout = GridLayoutManager(requireContext(), SPAN_SEARCH)
    private lateinit var callback: OnBackPressedCallback

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
                        searchMode()
                        true
                    }
                    else -> false
                }
            }
        })

        callback =
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, false) {
                moviesMode()
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            moviesFragment = this@MoviesFragment
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@MoviesFragment.viewModel
        }

        moviesAdapter = MoviesGenresAdapter(this) { adapter, genreId ->
            viewModel.associateAdapter(adapter, genreId)
        }

        searchAdapter = MoviesSearchAdapter(this)

        moviesAdapter.submitList(viewModel.genres)

        observeResults()
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

    private fun searchMode() {
        binding.apply {
            closeSearch.visibility = View.VISIBLE
            searchTv.visibility = View.VISIBLE
            moviesToolbarTitle.visibility = View.GONE
            hubMoviesGrid.searchMode()
        }

        callback.isEnabled = true
    }

    fun moviesMode() {
        binding.apply {
            closeSearch.visibility = View.GONE
            searchTv.visibility = View.GONE
            moviesToolbarTitle.visibility = View.VISIBLE
            searchTv.text.clear()
            hubMoviesGrid.moviesMode()
        }

        viewModel.clearSearchResult()
        callback.isEnabled = false

        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun RecyclerView.searchMode() {
        adapter = searchAdapter
        layoutManager = searchLayout
    }

    private fun RecyclerView.moviesMode() {
        adapter = moviesAdapter
        layoutManager = moviesLayout
    }

    private fun observeResults() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.searchedResults.collectLatest { movies ->
                searchAdapter.submitList(movies) {
                    searchLayout.scrollToPosition(0)
                }
            }
        }
    }

}