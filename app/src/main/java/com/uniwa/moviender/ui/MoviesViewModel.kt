package com.uniwa.moviender.ui

import android.view.View
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.uniwa.moviender.data.ServerPagingSource
import com.uniwa.moviender.network.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val service: MovienderApiService,
    private val _genres: List<Int>
) : ViewModel() {

    private val PAGE_SIZE = 15

    private val _movies = _genres.map { genre ->
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { ServerPagingSource(service, listOf(genre)) }
        ).flow.cachedIn(viewModelScope)
    }

    val movies = _movies

    val genres = _genres

    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie> = _selectedMovie

    private val _selectedMovieDetails = MutableLiveData<MovieDetails>()
    val selectedMovieDetails: LiveData<MovieDetails> = _selectedMovieDetails

    private fun getMovieDetails(movie: Movie) {
        viewModelScope.launch {
            _selectedMovieDetails.value = service.getMovieDetails(movie.movielensId, "123")
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
                    "123",
                    listOf(Rating(_selectedMovie.value!!.movielensId, rating))
                )
            )
        }
    }

}