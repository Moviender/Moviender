package com.uniwa.moviender.listener

import android.view.View
import android.view.animation.AccelerateInterpolator
import com.yuyakaido.android.cardstackview.*

class VotingButtonListener(
    private val manager: CardStackLayoutManager,
    private val direction: Direction,
    private val votingStackView: CardStackView
) : View.OnClickListener {
    override fun onClick(view: View?) {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(direction)
            .setDuration(Duration.Normal.duration)
            .setInterpolator(AccelerateInterpolator())
            .build()
        manager.setSwipeAnimationSetting(setting)
        votingStackView.swipe()
    }
}