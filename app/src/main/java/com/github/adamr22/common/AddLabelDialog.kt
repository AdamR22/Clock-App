package com.github.adamr22.common

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.github.adamr22.R
import com.github.adamr22.alarm.presentation.viewmodels.AlarmViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class AddLabelDialog(private val position: Int, private val viewModel: AlarmViewModel) : DialogFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it, R.style.AlertDialogButtonsLight)
            val inflater = requireActivity().layoutInflater
            builder
                .setView(inflater.inflate(R.layout.add_label_layout, null))
                .setTitle("Add Label")
                .setPositiveButton("OK") { _, _ ->
                    val label: String =
                        dialog?.findViewById<TextInputEditText>(R.id.add_label_edit_text)?.text.toString()
                    if (label.isNotEmpty()) viewModel.addLabel(label, position)
                }
                .setNegativeButton("CANCEL") { dialogInterface, _ ->
                    dialogInterface.cancel()
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int, viewModel: AlarmViewModel) = AddLabelDialog(position, viewModel)
    }
}