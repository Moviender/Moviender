package com.uniwa.moviender.listener

import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.view.updateLayoutParams
import com.google.android.material.card.MaterialCardView
import com.uniwa.moviender.R
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class VotingCardListener(
    private val newVote: (liked: Boolean) -> Unit
) : CardStackListener {

    override fun onCardDragging(direction: Direction?, ratio: Float) {}

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Left -> false
            Direction.Right -> true
            else -> null
        }?.let { liked ->
            newVote(liked)
        }
    }

    override fun onCardRewound() {}

    override fun onCardCanceled() {}

    override fun onCardAppeared(view: View?, position: Int) {}

    override fun onCardDisappeared(view: View?, position: Int) {
        view?.findViewById<ScrollView>(R.id.vote_movie_details)!!.visibility = View.GONE

        val voteMovieCard = view.findViewById<MaterialCardView>(R.id.vote_movie_card)
        voteMovieCard.collapse()
    }

    private fun MaterialCardView.collapse() {
        updateLayoutParams<ViewGroup.LayoutParams> {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }
}