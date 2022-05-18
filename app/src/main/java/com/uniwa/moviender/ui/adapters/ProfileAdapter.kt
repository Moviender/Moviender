package com.uniwa.moviender.ui.adapters

import android.view.*
import androidx.appcompat.view.menu.MenuView
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
                binding.requestAcceptBtn.visibility = View.GONE
                binding.requestRejectBtn.visibility = View.GONE
                binding.startSessionBtn.visibility = View.GONE
            } else if (friend.state == 2) {
                binding.requestPendingTv.visibility = View.GONE
                binding.startSessionBtn.visibility = View.GONE
                binding.requestAcceptBtn.visibility = View.VISIBLE
                binding.requestRejectBtn.visibility = View.VISIBLE
                binding.requestAcceptBtn.setOnClickListener {
                    viewModel.respondToFriendRequest(friend.uid, 1)
                }
                binding.requestRejectBtn.setOnClickListener {
                    viewModel.respondToFriendRequest(friend.uid, 0)
                }
            } else if (friend.state == 3) {
                binding.startSessionBtn.visibility = View.VISIBLE
                binding.requestPendingTv.visibility = View.GONE
                binding.requestAcceptBtn.visibility = View.GONE
                binding.requestRejectBtn.visibility = View.GONE
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

        if (friend.state == 3) {
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