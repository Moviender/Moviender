package com.uniwa.moviender.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.uniwa.moviender.HubNavigationDirections
import com.uniwa.moviender.R
import com.uniwa.moviender.adapters.bindPosterImage
import com.uniwa.moviender.databinding.SessionDialogBinding
import com.uniwa.moviender.ui.viewmodel.FriendsFragmentViewModel

class StartSessionDialogFragment : DialogFragment() {
    private lateinit var binding: SessionDialogBinding

    private val viewModel: FriendsFragmentViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.session_dialog,
            null,
            false
        )
        binding.lifecycleOwner = this
        binding.apply {
            svdBtn.setOnClickListener {
                val action = HubNavigationDirections.actionHubActivityToSessionActivity(1, viewModel.getFriendUid())
                findNavController().navigate(action)
                this@StartSessionDialogFragment.dismiss()
            }
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setView(binding.root)
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}