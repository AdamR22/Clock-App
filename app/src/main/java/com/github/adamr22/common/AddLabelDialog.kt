package com.github.adamr22.common

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.github.adamr22.R
import com.github.adamr22.alarm.AlarmViewModel
import com.google.android.material.textfield.TextInputEditText

class AddLabelDialog(private val position: Int) : DialogFragment() {
    private lateinit var alarmViewModel: AlarmViewModel

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            builder
                .setView(inflater.inflate(R.layout.add_label_layout, null))
                .setTitle("Add Label")
                .setPositiveButton("OK") { _, _ ->
                    val label: String =
                        dialog?.findViewById<TextInputEditText>(R.id.add_label_edit_text)?.text.toString()
                    alarmViewModel.addLabel(label, position)
                }
                .setNegativeButton("CANCEL") { dialogInterface, _ ->
                    dialogInterface.cancel()
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int) = AddLabelDialog(position)
    }
}