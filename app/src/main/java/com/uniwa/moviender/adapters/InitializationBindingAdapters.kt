package com.uniwa.moviender.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.ui.MovieGridAdapter
import java.lang.StringBuilder
import com.uniwa.moviender.data.genres

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Movie>?) {
    val adapter = recyclerView.adapter as MovieGridAdapter
    adapter.submitList(data)
}

@BindingAdapter("genreIds")
fun bindGenres(textView: TextView, genresIds: Array<Int>?) {
    val builder = StringBuilder()
    genresIds?.forEach { id -> builder.append(textView.resources.getString(genres[id]!!)).append(" ")}
    textView.text = builder.toString()
}