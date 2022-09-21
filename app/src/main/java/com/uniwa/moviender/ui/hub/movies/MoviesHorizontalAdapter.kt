package com.uniwa.moviender.ui.hub.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.databinding.MovieItemThumbnailBinding
import com.uniwa.moviender.network.Movie

class MoviesHorizontalAdapter(
    private val moviesFragment: MoviesFragment
) :
    PagingDataAdapter<Movie, MoviesHorizontalAdapter.MoviesViewHolder>(Diffcallback) {

        inner class MoviesViewHolder(
            private val binding: MovieItemThumbnailBinding
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(movie: Movie) {
                binding.movie = movie
                binding.moviesFragment = moviesFragment
                binding.executePendingBindings()
            }
        }

    companion object Diffcallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.movielensId == newItem.movielensId
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.movielensId == newItem.movielensId
        }

    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
            MovieItemThumbnailBinding.inflate(LayoutInflater.from(parent.context))
        )
    }
}