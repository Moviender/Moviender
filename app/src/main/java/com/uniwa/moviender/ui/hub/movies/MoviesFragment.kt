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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.uniwa.moviender.R
import com.uniwa.moviender.data.genres
import com.uniwa.moviender.databinding.FragmentMoviesBinding
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.ui.StartupActivityViewModel
import kotlinx.coroutines.cancel
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
    private lateinit var moviesLayout: LinearLayoutManager
    private lateinit var searchLayout: GridLayoutManager
    private lateinit var callback: OnBackPressedCallback
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

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

        setMoviesBottomSheetBehavior()

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

        moviesLayout = LinearLayoutManager(requireContext())
        searchLayout = GridLayoutManager(requireContext(), SPAN_SEARCH)

        searchAdapter = MoviesSearchAdapter(this)

        moviesMode()

        observeResults()
        observeGenres()
        observeGenresForPopulation()
    }

    private fun setMoviesBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.moviesBottomSheet).apply {
            isFitToContents = false
            isHideable = false
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    fun showMovieInfo(movie: Movie) {
        hideKeyboard()
        binding.movieDetailsFrame.visibility = View.VISIBLE
        viewModel.setSelectedMovie(movie)
    }

    fun closeMovieDetailsFrame() {
        binding.movieDetailsFrame.visibility = View.GONE
    }

    fun updateRating(rating: Float) {
        viewModel.sendRating(rating)
    }

    private fun searchMode() {
        binding.apply {
            closeSearch.visibility = View.VISIBLE
            searchTv.visibility = View.VISIBLE
            moviesToolbarTitle.visibility = View.GONE
            genresFilters.visibility = View.GONE
            hubMoviesGrid.searchMode()
        }

        callback.isEnabled = true
    }

    fun moviesMode() {
        binding.apply {
            closeSearch.visibility = View.GONE
            searchTv.visibility = View.GONE
            moviesToolbarTitle.visibility = View.VISIBLE
            genresFilters.visibility = View.VISIBLE
            searchTv.text.clear()
            hubMoviesGrid.moviesMode()
        }

        viewModel.clearSearchResult()
        callback.isEnabled = false

        hideKeyboard()
    }

    fun showGenreFilters() {
        bottomSheetBehavior.apply {
            if (state == BottomSheetBehavior.STATE_EXPANDED)
                state = BottomSheetBehavior.STATE_COLLAPSED
            else
                state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun RecyclerView.searchMode() {
        adapter = searchAdapter
        layoutManager = searchLayout
    }

    private fun RecyclerView.moviesMode() {
        adapter = moviesAdapter
        layoutManager = moviesLayout
    }

    private fun ChipGroup.populate(selectedGenres: List<Int>) {
        genres.forEach { genre ->
            val chip = layoutInflater.inflate(R.layout.genre_chip, this, false) as Chip
            chip.apply {
                id = genre.key
                text = getString(genre.value)
                selectedGenres.find { it == genre.key }?.let { isChecked = true }
                setOnCheckedChangeListener { _, _ ->
                    if (this.isChecked)
                        viewModel.genreSelected(genre.key)
                    else
                        viewModel.genreUnselected(genre.key)
                }
                this@populate.addView(this@apply)
            }
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
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

    private fun observeGenres() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.selectedGenres.collectLatest { genres ->
                moviesAdapter.submitList(genres)
            }
        }
    }

    private fun observeGenresForPopulation() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.selectedGenres.collectLatest { genres ->
                binding.genresChips.populate(genres)
                cancel()
            }
        }
    }

}