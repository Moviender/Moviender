package com.uniwa.moviender.ui.hub.movies

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.data.Genres
import com.uniwa.moviender.data.ServerPagingSource
import com.uniwa.moviender.network.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val PAGE_SIZE = 15

class MoviesViewModel(
    private val uid: String
) : ViewModel() {

    private val _genres = listOf(
        Genres.PERSONALIZED_RECOMMENDATIONS.code,
        Genres.ACTION.code,
        Genres.ANIMATION.code,
        Genres.CRIME.code,
        Genres.DRAMA.code,
        Genres.HORROR.code,
        Genres.MYSTERY.code,
        Genres.SCI_FI.code,
        Genres.WESTERN.code
    )

    private val _movies = _genres.map { genre ->
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                ServerPagingSource(
                    MovienderApi.movieClient,
                    listOf(genre),
                    uid
                )
            }
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
            _selectedMovieDetails.value =
                MovienderApi.movieClient.getMovieDetails(movie.movielensId, uid).body
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

    fun submitData(adapter: MoviesGenreSpecificAdapter, dataPosition: Int) {
        viewModelScope.launch {
            _movies[dataPosition].collectLatest { pageData ->
                adapter.submitData(pageData)
            }
        }
    }

    fun sendRating(rating: Float) {
        viewModelScope.launch {
            MovienderApi.movieClient.updateRating(
                UserRatings(
                    uid,
                    listOf(Rating(_selectedMovie.value!!.movielensId, rating))
                )
            )
        }
    }

    fun searchByTitle(title: String) {
        viewModelScope.launch {
            _searchedResults.value = MovienderApi.movieClient.searchByTitle(title).body
        }
    }

    fun clearSearchResult() {
        _searchedResults.value = listOf()
    }


    fun changeLayoutManager(layoutManager: RecyclerView.LayoutManager) {
        _layoutManager.value = layoutManager
    }

}