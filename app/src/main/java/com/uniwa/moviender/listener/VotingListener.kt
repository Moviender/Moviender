package com.uniwa.moviender.listener

import android.view.View
import android.widget.ScrollView
import com.uniwa.moviender.R
import com.uniwa.moviender.ui.viewmodel.VotingViewModel
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class VotingListener(
    private val viewModel: VotingViewModel
) : CardStackListener {

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Left -> {
                viewModel.newVote( false)
            }
            Direction.Right -> {
                viewModel.newVote(true)
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
        view.findViewById<View>(R.id.vote_movie_bottom_space).visibility = View.VISIBLE
        view.findViewById<View>(R.id.vote_movie_top_space).visibility = View.VISIBLE
    }
}