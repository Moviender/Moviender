package com.uniwa.moviender.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.data.genres
import com.uniwa.moviender.databinding.FragmentMoviesBinding
import com.uniwa.moviender.databinding.MovieCategoryRowBinding

class MoviesGridAdapter(
    private val context: Context,
    private val viewModel: MoviesViewModel
) : ListAdapter<Int, MoviesGridAdapter.MoviesGridViewHolder>(Diffcallback) { // TODO: check for list adapter

    inner class MoviesGridViewHolder(
        private val binding: MovieCategoryRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataPosition: Int) {
            val adapter = MoviesHorizontalAdapter()
            binding.movieCategory.text = genres[viewModel.genres[dataPosition]]?.let {
                binding.movieCategory.resources.getText(
                    it
                )
            }
            binding.movieRowRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.movieRowRv.adapter = adapter
            binding.executePendingBindings()
            viewModel.submitData(adapter, dataPosition)
        }
    }

    companion object Diffcallback : DiffUtil.ItemCallback<Int>() {
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
        holder.bind(position)
    }
}