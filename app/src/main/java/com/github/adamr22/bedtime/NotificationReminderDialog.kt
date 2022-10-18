package com.github.adamr22.bedtime

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.github.adamr22.R

class NotificationReminderDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setView(R.layout.notification_reminder_dialog_layout)
            .create()
    }

    companion object {
        const val TAG = "NotificationReminderDialog"
    }
}