package com.uniwa.moviender.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.FragmentFriendsBinding
import com.uniwa.moviender.ui.adapters.ProfileAdapter
import com.uniwa.moviender.ui.viewmodel.FriendsFragmentViewModel
import com.uniwa.moviender.ui.viewmodel.HubViewModel
import com.uniwa.moviender.ui.viewmodel.HubViewModelFactory


class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding
    private lateinit var dialog: FriendRequestDialogFragment

    private val sharedViewModel: HubViewModel by activityViewModels {
        HubViewModelFactory(findNavController())
    }
    private val viewModel: FriendsFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false)

        registerForContextMenu(binding.fragmentFriendsRv)
        viewModel.setUid(sharedViewModel.uid)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            friendsFragment = this@FriendsFragment
            fragmentFriendsRv.adapter =
                ProfileAdapter(this@FriendsFragment.viewModel, this@FriendsFragment)
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@FriendsFragment.viewModel
            friendsSwipeRefresh.setOnRefreshListener {
                this@FriendsFragment.viewModel.getFriends()
                friendsSwipeRefresh.isRefreshing = false
            }

        }

        viewModel.getFriends()

        viewModel.requestResponse.observe(viewLifecycleOwner) {
            checkResponse()
        }
    }

    private fun checkResponse() {
        when (viewModel.requestResponse.value) {
            1 -> {
                dialog.dismiss()
                viewModel.getFriends()
            }
            -1, -2, -3 -> {
                viewModel.setError(true)
            }
        }
    }

    fun showFriendRequestDialog() {
        dialog = FriendRequestDialogFragment()
        dialog.show(childFragmentManager, "request")
    }

    fun showSessionDialog() {
        StartSessionDialogFragment().show(childFragmentManager, "session")
    }

}