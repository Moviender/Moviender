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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.uniwa.moviender.R
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

    private lateinit var refreshListener: OnRefreshListener

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

        refreshListener = OnRefreshListener {
            viewModel.fetchFriends()
            binding.friendsSwipeRefresh.isRefreshing = false
        }

        binding.apply {
            friendsFragment = this@FriendsFragment
            fragmentFriendsRv.adapter =
                ProfileAdapter(this@FriendsFragment.viewModel, this@FriendsFragment)
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@FriendsFragment.viewModel
            friendsSwipeRefresh.setOnRefreshListener(refreshListener)
        }
    }

    override fun onResume() {
        super.onResume()

        refreshListener.onRefresh()
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

    private fun observeSessionState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.sessionState.collectLatest { sessionStatus ->
                sharedViewModel.sessionId = viewModel.sessionId

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
            viewModel.userState.collectLatest { userState ->
                val action = FriendsFragmentDirections.actionNavigationFriendsToSessionNavigation(
                    userState
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