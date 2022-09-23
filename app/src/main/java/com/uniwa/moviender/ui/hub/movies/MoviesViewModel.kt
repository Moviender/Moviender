package com.uniwa.moviender.ui.hub.movies

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.uniwa.moviender.data.Genres
import com.uniwa.moviender.data.ServerPagingSource
import com.uniwa.moviender.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val PAGE_SIZE = 15

class MoviesViewModel(
    private val uid: String
) : ViewModel() {

    val genres = listOf(
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

    private val _movies = hashMapOf<Int, Flow<PagingData<Movie>>>()

    init {
        genres.forEach { genre ->
            _movies[genre] = Pager(
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
    }

    var movies = _movies

    private val _searchedResults = MutableSharedFlow<List<Movie>>()
    val searchedResults: SharedFlow<List<Movie>> = _searchedResults

    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie> = _selectedMovie

    private val _selectedMovieDetails = MutableLiveData<MovieDetails>()
    val selectedMovieDetails: LiveData<MovieDetails> = _selectedMovieDetails

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

    fun associateAdapter(adapter: MoviesGenreSpecificAdapter, genreId: Int) {
        viewModelScope.launch {
            _movies[genreId]?.collectLatest { pageData ->
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
        viewModelScope.launch(Dispatchers.IO) {
            MovienderApi.movieClient.searchByTitle(title).let { response ->
                if (response.isSuccessful)
                    _searchedResults.emit(response.body)
            }
        }
    }

    fun clearSearchResult() {
        viewModelScope.launch(Dispatchers.Default) {
            _searchedResults.emit(listOf())
        }
    }

}