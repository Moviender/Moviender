package com.uniwa.moviender.listener

import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import com.google.android.material.card.MaterialCardView
import com.uniwa.moviender.R
import com.uniwa.moviender.ui.adapters.VoteCardStackViewAdapter
import com.uniwa.moviender.ui.viewmodel.VotingViewModel
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class VotingCardListener(
    private val viewModel: VotingViewModel,
    private val adapter: VoteCardStackViewAdapter
) : CardStackListener {

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Left -> {
                viewModel.newVote( false, adapter.itemCount)
            }
            Direction.Right -> {
                viewModel.newVote(true, adapter.itemCount)
            }
        }
    }

    override fun onCardRewound() {

    }

    override fun onCardCanceled() {

    }

    override fun onCardAppeared(view: View?, position: Int) {

    }

    override fun onCardDisappeared(view: View?, position: Int) {

        view?.findViewById<ScrollView>(R.id.vote_movie_details)!!.visibility = View.GONE

        val voteMovieCard = view.findViewById<MaterialCardView>(R.id.vote_movie_card)
        setCardViewHeightToWrapContent(voteMovieCard)

    }

    private fun setCardViewHeightToWrapContent(voteMovieCard: MaterialCardView) {
        val Mylayoutparams = voteMovieCard.layoutParams
        Mylayoutparams.height = WRAP_CONTENT
        voteMovieCard.layoutParams = Mylayoutparams
    }

    companion object {
        private const val WRAP_CONTENT =  ViewGroup.LayoutParams.WRAP_CONTENT
    }
}