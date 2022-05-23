package com.uniwa.moviender.listener

import android.view.View
import com.uniwa.moviender.ui.viewmodel.VotingViewModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
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

    }
}