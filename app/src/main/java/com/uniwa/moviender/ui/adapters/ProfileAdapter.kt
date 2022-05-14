package com.uniwa.moviender.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.databinding.ProfileItemBinding
import com.uniwa.moviender.model.Friend
import com.uniwa.moviender.ui.viewmodel.FriendsFragmentViewModel

class ProfileAdapter(
    private val viewModel: FriendsFragmentViewModel
) : ListAdapter<Friend, ProfileAdapter.ProfileViewHolder>(Diffcallback) {

    inner class ProfileViewHolder(
        private val binding: ProfileItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend) {
            binding.friend = friend
            if (friend.state == 1) {
                binding.requestPendingTv.visibility = View.VISIBLE
            }
            else if (friend.state == 2) {
                binding.requestAcceptBtn.visibility = View.VISIBLE
                binding.requestRejectBtn.visibility = View.VISIBLE
                binding.requestAcceptBtn.setOnClickListener {
                    viewModel.respondToFriendRequest(friend.uid, 1)
                    viewModel.getFriends()
                }
                binding.requestRejectBtn.setOnClickListener {
                    viewModel.respondToFriendRequest(friend.uid, 0)
                    viewModel.getFriends()
                }
            }
            binding.executePendingBindings()
        }
    }

    companion object Diffcallback : DiffUtil.ItemCallback<Friend>() {
        override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
            return oldItem.state == newItem.state
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        return ProfileViewHolder(
            ProfileItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}