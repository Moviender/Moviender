package com.uniwa.moviender.ui.hub.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.data.genres
import com.uniwa.moviender.databinding.MovieCategoryRowBinding

class MoviesGenresAdapter(
    private val moviesFragment: MoviesFragment,
    private val associateAdapter: (adapter: MoviesGenreSpecificAdapter, genreId: Int) -> Unit
) : ListAdapter<Int, MoviesGenresAdapter.MoviesGridViewHolder>(DiffCallback) {

    inner class MoviesGridViewHolder(
        private val binding: MovieCategoryRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(genreId: Int) {
            binding.genreResourceId = genres[genreId]!!

            MoviesGenreSpecificAdapter(moviesFragment).also { adapter ->
                associateAdapter(adapter, genreId)
                binding.movieRowRv.adapter = adapter
            }

            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesGridViewHolder {
        return MoviesGridViewHolder(
            MovieCategoryRowBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: MoviesGridViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}