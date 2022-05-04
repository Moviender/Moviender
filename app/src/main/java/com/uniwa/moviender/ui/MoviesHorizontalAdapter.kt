package com.uniwa.moviender.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.databinding.MovieItemThumbnailBinding
import com.uniwa.moviender.network.Movie

class MoviesHorizontalAdapter :
    PagingDataAdapter<Movie, MoviesHorizontalAdapter.MoviesViewHolder>(Diffcallback) {

        inner class MoviesViewHolder(
            private val binding: MovieItemThumbnailBinding
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(movie: Movie) {
                binding.movie = movie
                binding.executePendingBindings()
            }
        }

    companion object Diffcallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.movielensId == newItem.movielensId
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.title == newItem.title
        }

    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = getItem(position)
        movie?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
            MovieItemThumbnailBinding.inflate(LayoutInflater.from(parent.context))
        )
    }
}