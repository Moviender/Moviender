package com.uniwa.moviender.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.uniwa.moviender.database.session.Movie
import com.uniwa.moviender.databinding.MovieVotingItemBinding

class VoteCardStackViewAdapter :
    PagingDataAdapter<Movie, VoteCardStackViewAdapter.MovieVoteViewHolder>(Diffcallback) {

    inner class MovieVoteViewHolder(
        private val binding: MovieVotingItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movie = movie
            binding.voteMoviePoster.setOnClickListener { cardView ->
                if (binding.voteMovieOverview.visibility == View.GONE) {
                    TransitionManager.beginDelayedTransition(
                        cardView.parent as ViewGroup,
                        AutoTransition()
                    )
                    binding.voteMovieOverview.visibility = View.VISIBLE
                }
                else {
                    TransitionManager.beginDelayedTransition(
                        cardView.parent as ViewGroup,
                        AutoTransition()
                    )
                    binding.voteMovieOverview.visibility = View.GONE
                }
            }
            binding.executePendingBindings()
        }
    }

    companion object Diffcallback : DiffUtil.ItemCallback<Movie>() {
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
}