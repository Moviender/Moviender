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
                if (binding.voteMovieDetails.visibility == View.GONE) {
                    TransitionManager.beginDelayedTransition(
                        binding.voteMovieCard,
                        AutoTransition()
                    )
                    binding.voteMovieOverview.visibility = View.VISIBLE
                    binding.voteMovieDetails.visibility = View.VISIBLE
                    binding.voteMovieTopSpace.visibility = View.GONE
                    binding.voteMovieBottomSpace.visibility = View.GONE
                    setCardHeight(binding, 0)
                } else {
                    TransitionManager.beginDelayedTransition(
                        binding.voteMovieCard,
                        AutoTransition()
                    )
                    binding.voteMovieOverview.visibility = View.GONE
                    binding.voteMovieDetails.visibility = View.GONE
                    binding.voteMovieTopSpace.visibility = View.VISIBLE
                    binding.voteMovieBottomSpace.visibility = View.VISIBLE
                    setCardHeight(binding, WRAP_CONTENT)
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

        private const val WRAP_CONTENT =  ViewGroup.LayoutParams.WRAP_CONTENT

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

    private fun setCardHeight(binding: MovieVotingItemBinding, height: Int){
        val Mylayoutparams = binding.voteMovieCard.layoutParams
        Mylayoutparams.height = height
        binding.voteMovieCard.layoutParams = Mylayoutparams
    }

}