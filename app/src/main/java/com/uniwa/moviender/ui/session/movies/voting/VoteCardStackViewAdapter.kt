package com.uniwa.moviender.ui.session.movies.voting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.card.MaterialCardView
import com.uniwa.moviender.database.session.Movie
import com.uniwa.moviender.databinding.MovieVotingItemBinding

class VoteCardStackViewAdapter :
    PagingDataAdapter<Movie, VoteCardStackViewAdapter.MovieVoteViewHolder>(DiffCallback) {

    inner class MovieVoteViewHolder(
        private val binding: MovieVotingItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movie = movie
            binding.voteMovieCard.setOnClickListener {
                val voteMovieCard = it as MaterialCardView

                TransitionManager.beginDelayedTransition(
                    binding.voteMovieCard,
                    AutoTransition()
                )

                if (binding.voteMovieDetails.visibility == View.GONE)
                    voteMovieCard.expand(binding)
                else
                    voteMovieCard.collapse(binding)
            }

            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.movielensId == newItem.movielensId
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.overview == newItem.overview
        }
    }

    override fun onBindViewHolder(holder: MovieVoteViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVoteViewHolder {
        return MovieVoteViewHolder(
            MovieVotingItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    private fun MaterialCardView.expand(binding: MovieVotingItemBinding) {
        binding.apply {
            voteMovieOverview.visibility = View.VISIBLE
            voteMovieDetails.visibility = View.VISIBLE
        }

        updateLayoutParams<ViewGroup.LayoutParams> {
            height = 0
        }
    }

    private fun MaterialCardView.collapse(binding: MovieVotingItemBinding) {
        binding.apply {
            voteMovieOverview.visibility = View.GONE
            voteMovieDetails.visibility = View.GONE
        }

        updateLayoutParams<ViewGroup.LayoutParams> {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

}