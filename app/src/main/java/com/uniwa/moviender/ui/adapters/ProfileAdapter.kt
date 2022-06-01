package com.uniwa.moviender.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uniwa.moviender.data.FriendRequestStatus
import com.uniwa.moviender.data.FriendState
import com.uniwa.moviender.databinding.ProfileItemBinding
import com.uniwa.moviender.model.Friend
import com.uniwa.moviender.ui.fragment.FriendsFragment
import com.uniwa.moviender.ui.viewmodel.FriendsFragmentViewModel

class ProfileAdapter(
    private val viewModel: FriendsFragmentViewModel,
    private val friendsFragment: FriendsFragment
) : ListAdapter<Friend, ProfileAdapter.ProfileViewHolder>(Diffcallback) {

    inner class ProfileViewHolder(
        private val binding: ProfileItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend) {
            binding.friend = friend
            if (friend.state == FriendState.PENDING.code) {
                binding.requestPendingTv.visibility = View.VISIBLE
                binding.requestAcceptBtn.visibility = View.GONE
                binding.requestRejectBtn.visibility = View.GONE
                binding.startSessionBtn.visibility = View.GONE
                binding.showSessionBtn.visibility = View.GONE
                binding.closeSessionBtn.visibility = View.GONE
            } else if (friend.state == FriendState.REQUEST.code) {
                binding.requestPendingTv.visibility = View.GONE
                binding.startSessionBtn.visibility = View.GONE
                binding.showSessionBtn.visibility = View.GONE
                binding.closeSessionBtn.visibility = View.GONE
                binding.requestAcceptBtn.visibility = View.VISIBLE
                binding.requestRejectBtn.visibility = View.VISIBLE
                binding.requestAcceptBtn.setOnClickListener {
                    viewModel.respondToFriendRequest(
                        friend.uid,
                        FriendRequestStatus.ACCEPT_REQUEST.code
                    )
                }
                binding.requestRejectBtn.setOnClickListener {
                    viewModel.respondToFriendRequest(
                        friend.uid,
                        FriendRequestStatus.DECLINE_REQUEST.code
                    )
                }
            } else if (friend.state == FriendState.FRIEND.code) {
                binding.requestPendingTv.visibility = View.GONE
                binding.requestAcceptBtn.visibility = View.GONE
                binding.requestRejectBtn.visibility = View.GONE
                binding.showSessionBtn.visibility = View.GONE
                binding.closeSessionBtn.visibility = View.GONE
                binding.startSessionBtn.visibility = View.VISIBLE
                binding.startSessionBtn.setOnClickListener {
                    viewModel.setFriendUid(friend.uid)
                    friendsFragment.showSessionDialog()
                }
            } else if (friend.state == FriendState.SESSION.code) {
                binding.requestPendingTv.visibility = View.GONE
                binding.requestAcceptBtn.visibility = View.GONE
                binding.requestRejectBtn.visibility = View.GONE
                binding.startSessionBtn.visibility = View.GONE
                binding.showSessionBtn.visibility = View.VISIBLE
                binding.showSessionBtn.setOnClickListener {
                    viewModel.setFriendUid(friend.uid)
                    friendsFragment.navigate()
                }
                binding.closeSessionBtn.visibility = View.VISIBLE
                binding.closeSessionBtn.setOnClickListener {
                    viewModel.setFriendUid(friend.uid)
                    viewModel.closeSession()
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
            ProfileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val friend = getItem(position)

        if (friend.state == FriendState.FRIEND.code || friend.state == FriendState.SESSION.code) {
            addDeleteOption(holder.itemView, friend)
        }

        holder.bind(friend)
    }

    private fun addDeleteOption(itemView: View, friend: Friend) {
        itemView.setOnCreateContextMenuListener { contextMenu, view, contextMenuInfo ->
            contextMenu.add("Delete")
                .setOnMenuItemClickListener {
                    viewModel.deleteFriend(friend)

                    true
                }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}