package com.uniwa.moviender.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.uniwa.moviender.R
import com.uniwa.moviender.databinding.FriendRequestDialogBinding
import com.uniwa.moviender.model.ResponseCode
import com.uniwa.moviender.ui.viewmodel.FriendsFragmentViewModel

class FriendRequestDialogFragment : DialogFragment() {

    private lateinit var binding: FriendRequestDialogBinding

    private val viewModel: FriendsFragmentViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.friend_request_dialog,
            null,
            false
        )
        binding.viewModel = this@FriendRequestDialogFragment.viewModel
        binding.lifecycleOwner = this

        return activity?.let { fragmentActivity ->
            val builder = AlertDialog.Builder(fragmentActivity)


            builder.setView(binding.root)
                .setPositiveButton(R.string.dialog_add_friend,
                    DialogInterface.OnClickListener { dialog, id ->
                        viewModel.addFriend()
                    })
                .setNegativeButton(R.string.dialog_cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}