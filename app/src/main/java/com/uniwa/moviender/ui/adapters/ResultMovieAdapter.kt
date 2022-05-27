package com.uniwa.moviender.ui.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.database.session.Movie
import com.uniwa.moviender.databinding.ResultMovieItemBinding

class ResultMovieAdapter :
    ListAdapter<Movie, ResultMovieAdapter.ResultMovieViewHolder>(Diffcallback) {

    inner class ResultMovieViewHolder(
        private val binding: ResultMovieItemBinding
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
            // TODO compare user rating
            return oldItem.voteAverage == newItem.voteAverage
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