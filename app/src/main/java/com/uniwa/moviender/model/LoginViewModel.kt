package com.uniwa.moviender.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.network.MovienderApi
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uid = MutableLiveData<String>("")
    val uid: LiveData<String> = _uid

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    fun setUid(uid: String) {
        _uid.value = uid

        viewModelScope.launch {
            MovienderApi.movienderApiService.insertUser(User(uid))
        }
    }

    fun getStarterMovies() {
        viewModelScope.launch {
            _movies.value = MovienderApi.movienderApiService.getStarterMovies()
        }
    }
}