package com.uniwa.moviender.ui.session.movies.similar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.databinding.SimilarMovieItemSearchBinding
import com.uniwa.moviender.network.Movie

class SimilarMoviesAdapter(
    private val selectMovie: (movie: Movie) -> Unit
) : ListAdapter<Movie, SimilarMoviesAdapter.SimilarMoviesViewHolder>(DiffCallback)  {

    inner class SimilarMoviesViewHolder(
        private val binding: SimilarMovieItemSearchBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind (movie: Movie) {
            binding.movie = movie

            binding.resultMoviePosterIv.setOnClickListener {
                selectMovie(movie)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.movielensId == newItem.movielensId
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.movielensId == newItem.movielensId
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMoviesViewHolder {
        return SimilarMoviesViewHolder(
            SimilarMovieItemSearchBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: SimilarMoviesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}