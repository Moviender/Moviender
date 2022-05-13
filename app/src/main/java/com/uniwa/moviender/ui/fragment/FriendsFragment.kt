package com.uniwa.moviender.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.FragmentFriendsBinding
import com.uniwa.moviender.ui.adapters.ProfileAdapter
import com.uniwa.moviender.ui.viewmodel.FriendsFragmentViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding

    private val viewModel: FriendsFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragmentFriendsRv.adapter = ProfileAdapter()
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@FriendsFragment.viewModel
        }

            viewModel.getFriends()
    }
}