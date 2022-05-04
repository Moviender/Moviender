package com.uniwa.moviender.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.uniwa.moviender.data.ServerPagingSource
import com.uniwa.moviender.network.MovienderApiService

class MoviesViewModel(
    private val service: MovienderApiService,
    private val genres: List<Int>
) : ViewModel() {

    private val _movies =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { ServerPagingSource(service, genres) }
        ).flow.cachedIn(viewModelScope)
    val movies = _movies

    companion object {
        const val PAGE_SIZE = 15
    }
}