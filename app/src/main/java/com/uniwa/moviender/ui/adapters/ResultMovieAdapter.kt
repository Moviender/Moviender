package com.uniwa.moviender.ui.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.uniwa.moviender.databinding.ResultMovieItemBinding
import com.uniwa.moviender.network.Movie
import com.uniwa.moviender.ui.fragment.SessionMoviesFragment

class ResultMovieAdapter(
    private val fragment: SessionMoviesFragment
) :
    ListAdapter<Movie, ResultMovieAdapter.ResultMovieViewHolder>(Diffcallback) {

    inner class ResultMovieViewHolder(
        private val binding: ResultMovieItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movie = movie
            binding.sessionMoviesFragment = fragment
            binding.movieDetailsRating.tag = movie.movielensId
            binding.movieDetailsRating.rating = movie.movieDetails!!.userRating
            binding.executePendingBindings()
        }
    }

    companion object Diffcallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.movielensId == newItem.movielensId
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            // TODO compare user rating
            return oldItem.movieDetails?.userRating == newItem.movieDetails?.userRating
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultMovieViewHolder {
        return ResultMovieViewHolder(
            ResultMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ResultMovieViewHolder, position: Int) {
        val movie = getItem(position)

        holder.bind(movie)
    }
}