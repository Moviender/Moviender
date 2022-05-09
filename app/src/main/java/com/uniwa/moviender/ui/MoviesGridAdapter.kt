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
import com.uniwa.moviender.ui.fragments.MoviesFragment

class MoviesGridAdapter(
    private val context: Context,
    private val viewModel: MoviesViewModel,
    private val moviesFragment: MoviesFragment,
) : ListAdapter<Int, MoviesGridAdapter.MoviesGridViewHolder>(Diffcallback) {

    inner class MoviesGridViewHolder(
        private val binding: MovieCategoryRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataPosition: Int) {
            binding.genreResourceId = genres[viewModel.genres[dataPosition]]!!
            val adapter = MoviesHorizontalAdapter(moviesFragment)
            viewModel.submitData(adapter, dataPosition)
            binding.movieRowRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.movieRowRv.adapter = adapter
            binding.executePendingBindings()
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