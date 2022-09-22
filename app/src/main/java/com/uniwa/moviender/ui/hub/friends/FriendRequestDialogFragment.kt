package com.uniwa.moviender.ui.hub.friends

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.uniwa.moviender.R
import com.uniwa.moviender.data.FriendRequestStatus
import com.uniwa.moviender.databinding.FriendRequestDialogBinding
import kotlinx.coroutines.flow.collectLatest

class FriendRequestDialogFragment : DialogFragment() {

    private lateinit var binding: FriendRequestDialogBinding

    private val viewModel: FriendsFragmentViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private lateinit var dialogView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.friend_request_dialog,
            null,
            false
        )
        binding.viewModel = this@FriendRequestDialogFragment.viewModel
        binding.lifecycleOwner = this

        dialogView = binding.root

        val dialog = activity?.let { fragmentActivity ->
            val builder = AlertDialog.Builder(fragmentActivity)

            builder.setView(binding.root)
                .setPositiveButton(R.string.dialog_add_friend, null)
                .setNegativeButton(R.string.dialog_cancel) { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

        dialog.window?.setDimAmount(0.2f)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return dialogView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFriendRequest()
    }

    override fun onStart() {
        /* super.onStart() is where dialog.show()
           is actually called on the underlying dialog,
           so we have to do it after this point */
        super.onStart()
        val d = dialog as AlertDialog?
        if (d != null) {

            val positiveButton = d.getButton(Dialog.BUTTON_POSITIVE) as Button

            positiveButton.setOnClickListener {
                viewModel.addFriend()
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        // if user cancel reset text input and set error to false
        viewModel.clearUserInput()
        binding.usernameTi.isErrorEnabled = false
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // if user dismiss reset text input and set error to false
        viewModel.clearUserInput()
        binding.usernameTi.isErrorEnabled = false
    }

    private fun observeFriendRequest() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.friendRequestStatus.collectLatest { status ->
                when (status) {
                    FriendRequestStatus.SUCCESSFUL_FRIEND_REQUEST.code -> {
                        dismiss()
                        viewModel.fetchFriends()
                    }
                    FriendRequestStatus.USERNAME_NOT_FOUND.code -> {
                        binding.usernameTi.apply {
                            isErrorEnabled = true
                            error = getString(R.string.dialog_username_error_message)
                        }
                        binding.dialogFriendUsername.selectAll()
                    }
                    FriendRequestStatus.ALREADY_EXISTS.code -> {
                        binding.usernameTi.apply {
                            isErrorEnabled = true
                            error = getString(R.string.dialog_exists_error_message)
                        }
                        binding.dialogFriendUsername.selectAll()
                    }
                    FriendRequestStatus.SAME_UID.code -> {
                        binding.usernameTi.apply {
                            isErrorEnabled = true
                            error = getString(R.string.same_uid_error_message)
                        }
                        binding.dialogFriendUsername.selectAll()
                    }
                }
            }
        }
    }
}