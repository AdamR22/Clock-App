package com.github.adamr22.bedtime

import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.github.adamr22.R
import com.github.adamr22.utils.PickAlarmInterface
import com.github.adamr22.utils.TimePicker
import com.github.adamr22.utils.VibrateSingleton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.flow.collectLatest

class BedTImeFragmentBottomSheet(val viewModel: BedTimeViewModel) : BottomSheetDialogFragment() {

    private val BEDTIME_TAG = "Bedtime"
    private val WAKEUP_TAG = "Wakeup"

    private val SELECT_TIME_TAG = "Select Time"

    private var inflateBedTimeLayout: Boolean? = null
    private var inflateWakeUpLayout: Boolean? = null

    private lateinit var scheduleTime: SwitchMaterial

    private lateinit var bottomSheetIcon: ImageView
    private lateinit var bottomSheetText: TextView

    private lateinit var bedtimeNotTextContent: LinearLayout
    private lateinit var wakeUpNotTextContent: LinearLayout

    private lateinit var bedtimeTextContent: LinearLayout
    private lateinit var reminderNotificationText: TextView
    private lateinit var reminderNotificationSetTime: TextView

    private lateinit var wakeUpTextContent: ConstraintLayout
    private lateinit var btnSunriseAlarm: SwitchMaterial
    private lateinit var tvDefaultSound: TextView
    private lateinit var btnVibrate: SwitchMaterial

    private lateinit var tvSetTime: TextView

    private val defaultRingtoneUri by lazy {
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    }

    private val defaultRingtoneTitle by lazy {
        RingtoneManager.getRingtone(requireContext(), defaultRingtoneUri).getTitle(requireContext())
    }

    private val timePicker by lazy {
        TimePicker.buildPicker(SELECT_TIME_TAG)
    }

    private var isVibrate = true

    private val pickAlarmInterface by lazy {
        requireActivity() as PickAlarmInterface
    }

    companion object {
        const val TAG = "BedTime Modal Bottom Sheet"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inflateBedTimeLayout = arguments?.getBoolean(BEDTIME_TAG)
        inflateWakeUpLayout = arguments?.getBoolean(WAKEUP_TAG)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_bedtime_fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scheduleTime = view.findViewById(R.id.set_time_switch)

        bottomSheetIcon = view.findViewById(R.id.bottom_sheet_icon)
        bottomSheetText = view.findViewById(R.id.bottom_sheet_text)

        bedtimeNotTextContent = view.findViewById(R.id.bedtime_not_text_content)
        wakeUpNotTextContent = view.findViewById(R.id.wakeup_not_text_content)

        bedtimeTextContent = view.findViewById(R.id.bed_time_text_content)
        reminderNotificationText = view.findViewById(R.id.reminder_notification_text) //Clickable
        reminderNotificationSetTime = view.findViewById(R.id.reminder_notification_set_time)

        wakeUpTextContent = view.findViewById(R.id.wakeup_text_content)
        btnSunriseAlarm = view.findViewById(R.id.btn_sunrise_alarm)
        tvDefaultSound = view.findViewById(R.id.tv_default_sound) // clickable
        btnVibrate = view.findViewById(R.id.btn_vibrate)

        tvSetTime = view.findViewById(R.id.tv_set_time)

        tvDefaultSound.text = defaultRingtoneTitle
    }

    override fun onResume() {
        super.onResume()

        renderUI()

        changeNotificationReminderText()

        scheduleTime.setOnClickListener {
            if (inflateBedTimeLayout!!) viewModel.scheduleBedTime()

            if (inflateWakeUpLayout!!) viewModel.scheduleWakeUpTime()
        }

        reminderNotificationText.setOnClickListener {
            NotificationReminderDialog(viewModel).show(
                parentFragmentManager,
                NotificationReminderDialog.TAG
            )
        }

        btnVibrate.isChecked = isVibrate

        btnVibrate.setOnCheckedChangeListener { btn, _ ->
            if (btn.isChecked) {
                isVibrate = true
                VibrateSingleton.vibrateDeviceOnce(requireContext(), true)
            }

            if (!btn.isChecked) {
                isVibrate = false
                VibrateSingleton.vibrateDeviceOnce(requireContext(), false)
            }
        }

        tvDefaultSound.setOnClickListener {
            pickAlarmInterface.selectAlarmTone()
        }

        if (pickAlarmInterface.returnSelectedTone() != null) {
            tvDefaultSound.text = pickAlarmInterface.returnSelectedTone()!!.second
        }

        tvSetTime.setOnClickListener {
            timePicker.show(parentFragmentManager, TAG)
            timePicker.addOnPositiveButtonClickListener {
                tvSetTime.text = String.format(
                    resources.getString(R.string.default_time_2),
                    "%02d".format(timePicker.hour),
                    "%02d".format(timePicker.minute)
                )
            }
        }
    }

    private fun renderUI() {
        renderBottomSheetText()
    }

    private fun renderBottomSheetText() {
        if (inflateBedTimeLayout!!) {
            bottomSheetIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_bedtime
                )
            )
            bottomSheetText.text = resources.getString(R.string.bedtime_capitalized)

            bedtimeNotTextContent.visibility = View.VISIBLE
            scheduleTime.isChecked = viewModel.bedTimeScheduled.value

            renderBedtimeTextContent()
        } else bedtimeNotTextContent.visibility = View.GONE

        if (inflateWakeUpLayout!!) {
            bottomSheetIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_bed
                )
            )
            bottomSheetText.text = resources.getString(R.string.wake_up)

            wakeUpNotTextContent.visibility = View.VISIBLE
            scheduleTime.isChecked = viewModel.wakeUpTimeScheduled.value

            renderWakeUpTextContent()
        } else wakeUpNotTextContent.visibility = View.GONE
    }

    private fun renderBedtimeTextContent() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.bedTimeScheduled.collectLatest {
                when (it) {
                    true -> {
                        bedtimeNotTextContent.visibility = View.GONE
                        bedtimeTextContent.visibility = View.VISIBLE
                    }

                    false -> {
                        bedtimeTextContent.visibility = View.GONE
                        bedtimeNotTextContent.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun renderWakeUpTextContent() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.wakeUpTimeScheduled.collectLatest {
                when (it) {
                    true -> {
                        wakeUpNotTextContent.visibility = View.GONE
                        wakeUpTextContent.visibility = View.VISIBLE
                    }

                    false -> {
                        wakeUpTextContent.visibility = View.GONE
                        wakeUpNotTextContent.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun changeNotificationReminderText() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.notificationReminderText.collectLatest {
                if (it.isNotEmpty()) reminderNotificationSetTime.text = it
            }
        }
    }

}