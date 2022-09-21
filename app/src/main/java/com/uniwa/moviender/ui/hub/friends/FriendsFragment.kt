package com.uniwa.moviender.ui.hub.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.uniwa.moviender.R
import com.uniwa.moviender.data.FriendRequestStatus
import com.uniwa.moviender.data.SessionStatus
import com.uniwa.moviender.databinding.FragmentFriendsBinding
import com.uniwa.moviender.ui.StartupActivityViewModel
import kotlinx.coroutines.flow.collectLatest


class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding
    private lateinit var dialog: FriendRequestDialogFragment

    private val sharedViewModel: StartupActivityViewModel by activityViewModels()
    private val viewModel: FriendsFragmentViewModel by viewModels {
        FriendsViewModelFactory(sharedViewModel.getUid())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false)
        registerForContextMenu(binding.fragmentFriendsRv)

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
                this@FriendsFragment.viewModel.fetchFriends()
                friendsSwipeRefresh.isRefreshing = false
            }

        }

        observeFriendRequest()
    }

    fun showFriendRequestDialog() {
        dialog = FriendRequestDialogFragment()
        dialog.show(childFragmentManager, "request")
    }

    fun showSessionDialog() {
        findNavController().navigate(R.id.action_navigation_friends_to_StartSessionDialogFragment)
    }

    fun navigateToSession() {
        observeSessionState()
        observeUserState()
        viewModel.getSessionState()
    }

    private fun observeFriendRequest() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.friendRequestStatus.collectLatest { status ->
                when (status) {
                    FriendRequestStatus.SUCCESSFUL_FRIEND_REQUEST.code -> {
                        dialog.dismiss()
                        viewModel.fetchFriends()
                    }
                    FriendRequestStatus.USERNAME_NOT_FOUND.code,
                    FriendRequestStatus.ALREADY_EXISTS.code,
                    FriendRequestStatus.SAME_UID.code -> {
                        viewModel.setError(true)
                    }
                }
            }
        }
    }

    private fun observeSessionState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.sessionState.collectLatest { sessionStatus ->
                sharedViewModel.sessionId = viewModel.sessionId.value!!
                when (sessionStatus) {
                    SessionStatus.WAITING_FOR_VOTES.code -> {
                        viewModel.getUserState()
                    }
                    SessionStatus.SUCCESSFUL_FINISH.code,
                    SessionStatus.FAILED_FINISH.code -> {
                        val action =
                            FriendsFragmentDirections.actionNavigationFriendsToSessionNavigation(
                                sessionStatus
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun observeUserState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.userState.collectLatest { userStatus ->
                val action = FriendsFragmentDirections.actionNavigationFriendsToSessionNavigation(
                    userStatus
                )
                findNavController().navigate(action)
            }
        }
    }

    fun setFriendUid(uid: String) {
        sharedViewModel.friendUid = uid
        viewModel.setFriendUid(uid)
    }
}