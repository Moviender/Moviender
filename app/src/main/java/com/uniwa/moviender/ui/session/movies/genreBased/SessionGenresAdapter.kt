package com.uniwa.moviender.ui.session.movies.genreBased

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.databinding.GenreItemBinding

class SessionGenresAdapter(
    private val sessionGenresFragment: SessionGenresFragment
) : ListAdapter<Int, SessionGenresAdapter.GenreViewHolder>(Diffcallback) {

    inner class GenreViewHolder(
        private val binding: GenreItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: Int) {
            binding.genre = genre
            binding.sessionGenresFragment = sessionGenresFragment
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            GenreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}