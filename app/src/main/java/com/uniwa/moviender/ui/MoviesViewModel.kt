package com.uniwa.moviender.ui

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.uniwa.moviender.data.ServerPagingSource
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.network.MovieDetails
import com.uniwa.moviender.network.MovienderApi
import com.uniwa.moviender.network.MovienderApiService
import kotlinx.coroutines.flow.Flow
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

}