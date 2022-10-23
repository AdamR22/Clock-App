package com.github.adamr22.bedtime.presentation.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.github.adamr22.R
import com.github.adamr22.bedtime.presentation.viewmodels.BedTimeViewModel
import com.github.adamr22.data.entities.Alarm
import com.github.adamr22.data.models.AlarmItemModel

@SuppressLint("InflateParams")
class NotificationReminderDialog(
    val viewModel: BedTimeViewModel,
    val data: AlarmItemModel,
) :
    DialogFragment() {
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

            when (radioButton.text.toString()) {
                resources.getString(R.string.fifteen_min_reminder) -> viewModel.updateTime(
                    Alarm(
                        data.id,
                        data.label,
                        data.ringtoneTitle,
                        data.ringtoneUri,
                        data.isScheduled,
                        data.isSunriseMode,
                        data.isVibrate,
                        data.hour,
                        data.minute,
                        15
                    )
                )
                resources.getString(R.string.thirty_min_reminder) -> viewModel.updateTime(
                    Alarm(
                        data.id,
                        data.label,
                        data.ringtoneTitle,
                        data.ringtoneUri,
                        data.isScheduled,
                        data.isSunriseMode,
                        data.isVibrate,
                        data.hour,
                        data.minute,
                        30
                    )
                )
                resources.getString(R.string.forty_five_min_reminder) -> viewModel.updateTime(
                    Alarm(
                        data.id,
                        data.label,
                        data.ringtoneTitle,
                        data.ringtoneUri,
                        data.isScheduled,
                        data.isSunriseMode,
                        data.isVibrate,
                        data.hour,
                        data.minute,
                        45
                    )
                )
                resources.getString(R.string.one_hr_reminder) -> viewModel.updateTime(
                    Alarm(
                        data.id,
                        data.label,
                        data.ringtoneTitle,
                        data.ringtoneUri,
                        data.isScheduled,
                        data.isSunriseMode,
                        data.isVibrate,
                        data.hour,
                        data.minute,
                        1
                    )
                )
            }
        }
    }

    companion object {
        const val TAG = "NotificationReminderDialog"
    }
}