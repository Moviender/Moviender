package com.uniwa.moviender.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.uniwa.moviender.R
import com.uniwa.moviender.data.RecommendationType
import com.uniwa.moviender.databinding.SessionDialogBinding

class StartSessionDialogFragment : DialogFragment() {
    private lateinit var binding: SessionDialogBinding

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
                val action =
                    StartSessionDialogFragmentDirections.actionStartSessionDialogFragmentToSessionNavigation(
                        RecommendationType.SVD.code
                    )
                findNavController().navigate(action)
            }
            knnBtn.setOnClickListener {
                val action =
                    StartSessionDialogFragmentDirections.actionStartSessionDialogFragmentToSessionNavigation(
                        RecommendationType.SVD.code
                    )
                findNavController().navigate(action)
            }
        }

        val dialog = activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setDimAmount(0.2f)
        return dialog
    }
}