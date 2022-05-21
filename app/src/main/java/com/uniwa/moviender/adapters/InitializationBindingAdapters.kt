package com.uniwa.moviender.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.textview.MaterialTextView
import com.uniwa.moviender.R
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.ui.adapters.MoviesRowAdapter
import com.uniwa.moviender.data.genres
import com.uniwa.moviender.model.Friend
import com.uniwa.moviender.model.User
import com.uniwa.moviender.ui.adapters.ProfileAdapter
import com.uniwa.moviender.ui.adapters.SessionGenresAdapter
import com.uniwa.moviender.ui.adapters.VoteCardStackViewAdapter
import com.yuyakaido.android.cardstackview.CardStackView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Movie>?) {
    val adapter = recyclerView.adapter as MoviesRowAdapter
    adapter.submitList(data)
}

@BindingAdapter("friendList")
fun bindFriendList(recyclerView: RecyclerView, friendList: List<Friend>?) {
    val adapter = recyclerView.adapter as ProfileAdapter
    adapter.submitList(friendList)
}

@BindingAdapter("genresList")
fun bindGenreList(recyclerView: RecyclerView, genres: List<Int>?) {
    val adapter = recyclerView.adapter as SessionGenresAdapter
    val list = genres?.sortedBy { genreId ->
        recyclerView.resources.getString(genreId)
    }
    adapter.submitList(list)
}

@BindingAdapter("idToString")
fun bindResourceId(materialTextView: MaterialTextView, resourceId: Int) {
    materialTextView.text = materialTextView.resources.getString(resourceId)
}

@BindingAdapter("genres")
fun bindGenres(textView: TextView, genresIds: Array<Int>?) {
    textView.text = genresIds?.map { genreId ->
        textView.resources.getString(genres[genreId]!!)
    }
        ?.joinToString(separator = " | ")
}

@BindingAdapter("imageUrl")
fun bindPosterImage(imageView: ImageView, filePath: String?) {
    val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500/"
    filePath?.let {
        val posterUri = POSTER_BASE_URL.plus(filePath).toUri().buildUpon().scheme("https").build()
        imageView.load(posterUri) {
            // TODO Images for error and loading
            placeholder(R.drawable.movies_poster_placeholder)
        }
    }
}