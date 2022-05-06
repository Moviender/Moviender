package com.uniwa.moviender.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.uniwa.moviender.data.ServerPagingSource
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.network.MovienderApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val service: MovienderApiService,
    private val _genres: List<Int>
) : ViewModel() {

    private val _movies = _genres.map { genre ->
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { ServerPagingSource(service, listOf(genre)) }
        ).flow.cachedIn(viewModelScope)
    }

    val movies = _movies

    val genres = _genres

    /*
    private val _movies2 =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { ServerPagingSource(service, genres) }
        ).flow.cachedIn(viewModelScope)
     */

    fun submitData(adapter: MoviesHorizontalAdapter, dataPosition: Int) {
        viewModelScope.launch {
            _movies[dataPosition].collectLatest { pageData ->
                adapter.submitData(pageData)
            }
        }
    }

    companion object {
        const val PAGE_SIZE = 15
    }
}