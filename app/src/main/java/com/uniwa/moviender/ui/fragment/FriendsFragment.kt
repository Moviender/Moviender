package com.uniwa.moviender.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.uniwa.moviender.HubNavigationDirections
import com.uniwa.moviender.R
import com.uniwa.moviender.data.FriendRequestStatus
import com.uniwa.moviender.data.SessionStatus
import com.uniwa.moviender.data.SessionUserStatus
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
            FriendRequestStatus.SUCCESSFUL_FRIEND_REQUEST.code -> {
                dialog.dismiss()
                viewModel.getFriends()
            }
            FriendRequestStatus.USERNAME_NOT_FOUND.code,
            FriendRequestStatus.ALREADY_EXISTS.code,
            FriendRequestStatus.SAME_UID.code -> {
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

    fun navigate() {
        viewModel.sessionId.observe(this) {
            viewModel.sessionState.observe(this) { sessionStatus ->
                if (sessionStatus == SessionStatus.WAITING_FOR_VOTES.code) {
                    viewModel.userState.observe(this) { userStatus ->
                        if (userStatus == SessionUserStatus.VOTING.code) {
                            val action = HubNavigationDirections.actionHubActivityToSessionActivity(
                                SessionUserStatus.VOTING.code,
                                viewModel.getFriendUid(),
                                viewModel.sessionId.value
                            )
                            findNavController().navigate(action)
                        } else if (userStatus == SessionUserStatus.WAITING.code) {
                            val action = HubNavigationDirections.actionHubActivityToSessionActivity(
                                SessionUserStatus.WAITING.code,
                                viewModel.getFriendUid(),
                                viewModel.sessionId.value
                            )
                            findNavController().navigate(action)
                        } else if (userStatus == SessionUserStatus.VOTING_AGAIN.code) {
                            val action = HubNavigationDirections.actionHubActivityToSessionActivity(
                                SessionUserStatus.VOTING_AGAIN.code,
                                viewModel.getFriendUid(),
                                viewModel.sessionId.value
                            )
                            findNavController().navigate(action)
                        }
                    }
                    viewModel.getUserState()
                } else if (sessionStatus == SessionStatus.SUCCESSFUL_FINISH.code) {
                    val action = HubNavigationDirections.actionHubActivityToSessionActivity(
                        SessionStatus.SUCCESSFUL_FINISH.code,
                        null,
                        viewModel.sessionId.value
                    )

                    findNavController().navigate(action)
                }
                else if (sessionStatus == SessionStatus.FAILED_FINISH.code) {
                    val action = HubNavigationDirections.actionHubActivityToSessionActivity(
                        SessionStatus.FAILED_FINISH.code,
                        null,
                        viewModel.sessionId.value
                    )
                    findNavController().navigate(action)
                }
            }
            viewModel.getSessionState()
        }
        viewModel.getSessionId()
    }

}