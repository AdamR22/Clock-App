package com.github.adamr22.bedtime

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.github.adamr22.R

@SuppressLint("InflateParams")
class NotificationReminderDialog(val viewModel: BedTimeViewModel) : DialogFragment() {
    private lateinit var radioGroup: RadioGroup

    private val dialogView by lazy {
        layoutInflater.inflate(
            R.layout.notification_reminder_dialog_layout,
            null
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setView(dialogView)
            .create()
    }

    override fun onResume() {
        super.onResume()

        radioGroup = dialogView.findViewById(R.id.notification_reminder_radio_group)

        radioGroup.setOnCheckedChangeListener { radioGroup, _ ->
            val selectedId = radioGroup.checkedRadioButtonId

            val radioButton = dialogView.findViewById<RadioButton>(selectedId)

            viewModel.notificationReminderText(radioButton.text.toString())
        }
    }

    companion object {
        const val TAG = "NotificationReminderDialog"
    }
}