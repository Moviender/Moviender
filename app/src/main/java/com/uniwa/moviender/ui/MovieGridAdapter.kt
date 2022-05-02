package com.uniwa.moviender.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import androidx.transition.Visibility
import com.uniwa.moviender.databinding.GridViewItemBinding
import com.uniwa.moviender.model.LoginViewModel
import com.uniwa.moviender.network.Movie

class MovieGridAdapter(private var viewModel: LoginViewModel) :
    ListAdapter<Movie, MovieGridAdapter.MoviesViewHolder>(Diffcallback) {

    class MoviesViewHolder(
        private var binding: GridViewItemBinding,
        private var viewModel: LoginViewModel
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(movie: Movie) {
                binding.movie = movie
                binding.viewModel = viewModel
                viewModel.addRatingBar(binding.ratingRb)
                viewModel.addMovieRatingBar(binding.ratingRb, movie.movielens_id)
                binding.executePendingBindings()
            }
        }

    companion object Diffcallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.movielens_id == newItem.movielens_id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.title == newItem.title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
            GridViewItemBinding.inflate(LayoutInflater.from(parent.context)),
            viewModel
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