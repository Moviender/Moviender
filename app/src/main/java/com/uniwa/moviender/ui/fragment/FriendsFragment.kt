package com.uniwa.moviender.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.FragmentFriendsBinding
import com.uniwa.moviender.ui.adapters.ProfileAdapter
import com.uniwa.moviender.ui.viewmodel.FriendsFragmentViewModel


class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding
    private lateinit var dialog: FriendRequestDialogFragment

    private val viewModel: FriendsFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            friendsFragment = this@FriendsFragment
            fragmentFriendsRv.adapter = ProfileAdapter()
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@FriendsFragment.viewModel
        }

        viewModel.getFriends()

        viewModel.requestResponse.observe(viewLifecycleOwner) {
            checkResponse()
        }
    }

    private fun checkResponse() {
        when(viewModel.requestResponse.value) {
            -1 -> {
                viewModel.setErrorMessage("Username not found")
                viewModel.setError(true)
            }
            1 -> {
                viewModel.setError(false)
                dialog.dismiss()
            }
        }
    }

    fun showFriendRequestDialog() {
        dialog = FriendRequestDialogFragment()
        dialog.show(childFragmentManager, "request")

    }
}