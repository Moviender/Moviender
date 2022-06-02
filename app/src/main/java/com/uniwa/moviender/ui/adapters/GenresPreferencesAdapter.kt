package com.uniwa.moviender.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.databinding.GenrePreferencesItemBinding
import com.uniwa.moviender.ui.fragment.GenresPreferencesFragment

class GenresPreferencesAdapter(
    private val genresPreferencesFragment: GenresPreferencesFragment
): ListAdapter<Int, GenresPreferencesAdapter.GenresPrefencesViewHolder>(Diffcallback) {

    inner class GenresPrefencesViewHolder(
        private val binding: GenrePreferencesItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(genre: Int) {
            binding.genre = genre
            binding.genresPreferencesFragment = genresPreferencesFragment
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresPrefencesViewHolder {
        return GenresPrefencesViewHolder(
            GenrePreferencesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: GenresPrefencesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // Because of glitch that checkboxes moved to other
    // position during item reloading (ex from scrolling)
    override fun getItemViewType(position: Int): Int {
        return position
    }

}