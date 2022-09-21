package com.uniwa.moviender.ui.initialization

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.databinding.GridViewItemBinding
import com.uniwa.moviender.network.Movie

class MoviesRowAdapter(private val viewModel: InitializationViewModel) :
    ListAdapter<Movie, MoviesRowAdapter.MoviesViewHolder>(Diffcallback) {

    inner class MoviesViewHolder(
        private var binding: GridViewItemBinding,
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(movie: Movie) {
                binding.movie = movie
                binding.viewModel = viewModel
                binding.ratingRb.tag = movie.movielensId
                viewModel.addRating(movie.movielensId)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
            GridViewItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    // Because of glitch at ratings
    override fun getItemViewType(position: Int): Int {
        return position
    }
}