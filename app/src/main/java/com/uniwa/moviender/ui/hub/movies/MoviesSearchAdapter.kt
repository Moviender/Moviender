package com.uniwa.moviender.ui.hub.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.databinding.MovieItemSearchThumbnailBinding
import com.uniwa.moviender.network.Movie

class MoviesSearchAdapter(
    private val moviesFragment: MoviesFragment
) :
    ListAdapter<Movie, MoviesSearchAdapter.MoviesViewHolder>(Diffcallback) {

    inner class MoviesViewHolder(
        private val binding: MovieItemSearchThumbnailBinding
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
            MovieItemSearchThumbnailBinding.inflate(LayoutInflater.from(parent.context))
        )
    }
}