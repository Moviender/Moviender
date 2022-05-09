package com.uniwa.moviender.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.uniwa.moviender.R
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.ui.MoviesRowAdapter
import java.lang.StringBuilder
import com.uniwa.moviender.data.genres

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Movie>?) {
    val adapter = recyclerView.adapter as MoviesRowAdapter
    adapter.submitList(data)
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