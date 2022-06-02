package com.uniwa.moviender.ui.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.data.ServerPagingSource
import com.uniwa.moviender.network.*
import com.uniwa.moviender.ui.adapters.MoviesHorizontalAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val service: MovienderApiService,
    _genres: List<Int>,
    private val uid: String
) : ViewModel() {

    private val PAGE_SIZE = 15

    private val _movies = _genres.map { genre ->
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { ServerPagingSource(service, listOf(genre)) }
        ).flow.cachedIn(viewModelScope)
    }

    var movies = _movies

    private val _searchedResults = MutableLiveData<List<Movie>>()
    val searchedResults: LiveData<List<Movie>> = _searchedResults

    val genres = _genres

    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie> = _selectedMovie

    private val _selectedMovieDetails = MutableLiveData<MovieDetails>()
    val selectedMovieDetails: LiveData<MovieDetails> = _selectedMovieDetails

    private val _layoutManager = MutableLiveData<RecyclerView.LayoutManager>()
    val layoutManager: LiveData<RecyclerView.LayoutManager> = _layoutManager

    private fun getMovieDetails(movie: Movie) {
        viewModelScope.launch {
            _selectedMovieDetails.value = service.getMovieDetails(movie.movielensId, uid)
        }
    }

    private val _frameVisibility = MutableLiveData<Int>(View.GONE)
    val frameVisibility: LiveData<Int> = _frameVisibility

    fun changeFrameVisibility(visibility: Int) {
        _frameVisibility.value = visibility
    }

    fun setSelectedMovie(movie: Movie) {
        getMovieDetails(movie)
        _selectedMovie.value = movie
        changeFrameVisibility(View.VISIBLE)
    }

    fun submitData(adapter: MoviesHorizontalAdapter, dataPosition: Int) {
        viewModelScope.launch {
            _movies[dataPosition].collectLatest { pageData ->
                adapter.submitData(pageData)
            }
        }
    }

    fun sendRating(rating: Float) {
        viewModelScope.launch {
            service.updateRating(
                UserRatings(
                    uid,
                    listOf(Rating(_selectedMovie.value!!.movielensId, rating))
                )
            )
        }
    }

    fun searchByTitle(title: String) {
        viewModelScope.launch {
            _searchedResults.value = service.searchByTitle(title)
        }
    }

    fun clearSearchResult() {
        _searchedResults.value = listOf()
    }


    fun changeLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        _layoutManager.value = layoutManager
    }

}