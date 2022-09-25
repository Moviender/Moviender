package com.uniwa.moviender.ui.hub.movies

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

    private val _selectedGenres = MutableSharedFlow<List<Int>>()
    val selectedGenres: SharedFlow<List<Int>> = _selectedGenres

    private val genrePreferences = mutableListOf(Genres.PERSONALIZED_RECOMMENDATIONS.code)

    private val _movies = hashMapOf<Int, Flow<PagingData<Movie>>>()

    init {
        getUserGenrePreferences()
    }

    var movies = _movies

    private val _searchedResults = MutableSharedFlow<List<Movie>>()
    val searchedResults: SharedFlow<List<Movie>> = _searchedResults

    private val _selectedMovie = MutableLiveData<Movie>()
    val selectedMovie: LiveData<Movie> = _selectedMovie

    private val _selectedMovieDetails = MutableLiveData<MovieDetails>()
    val selectedMovieDetails: LiveData<MovieDetails> = _selectedMovieDetails

    private fun getMovieInfo(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            MovienderApi.movieClient.getMovieDetails(movie.movielensId, uid).let { response ->
                if (response.isSuccessful)
                    _selectedMovieDetails.postValue(response.body)
            }
        }
    }

    private fun getUserGenrePreferences() {
        viewModelScope.launch(Dispatchers.IO) {
            MovienderApi.userClient.getGenrePreferences(uid).let { response ->
                if (response.isSuccessful) {
                    genrePreferences.addAll(response.body)

                    genrePreferences.forEach { genre ->
                        _movies[genre] = getGenrePager(genre)
                    }

                    _selectedGenres.emit(genrePreferences.toList())
                }
            }
        }
    }

    private fun getGenrePager(genreId: Int) = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = {
            ServerPagingSource(
                MovienderApi.movieClient,
                listOf(genreId),
                uid
            )
        }
    ).flow.cachedIn(viewModelScope)

    fun setSelectedMovie(movie: Movie) {
        getMovieInfo(movie)
        _selectedMovie.value = movie
    }

    fun associateAdapter(adapter: MoviesGenreSpecificAdapter, genreId: Int) {
        viewModelScope.launch {
            _movies[genreId]?.collectLatest { pageData ->
                adapter.submitData(pageData)
            }
        }
    }

    fun sendRating(rating: Float) {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun genreSelected(genreId: Int) {
        _movies[genreId] = getGenrePager(genreId)

        genrePreferences.add(genreId)
        viewModelScope.launch {
            _selectedGenres.emit(genrePreferences.toList())
        }
    }

    fun genreUnselected(genreId: Int) {
        _movies.remove(genreId)

        genrePreferences.remove(genreId)
        viewModelScope.launch {
            _selectedGenres.emit(genrePreferences.toList())
        }
    }

}