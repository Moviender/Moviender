package com.uniwa.moviender.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.databinding.GenreItemBinding
import com.uniwa.moviender.model.Friend

class SessionGenresAdapter : ListAdapter<String, SessionGenresAdapter.GenreViewHolder>(Diffcallback) {

    inner class GenreViewHolder(
        private val binding: GenreItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: String) {
            binding.genre = genre
        }
    }

    companion object Diffcallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
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