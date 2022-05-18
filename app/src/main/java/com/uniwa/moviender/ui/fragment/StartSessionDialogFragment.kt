package com.uniwa.moviender.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.uniwa.moviender.R
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