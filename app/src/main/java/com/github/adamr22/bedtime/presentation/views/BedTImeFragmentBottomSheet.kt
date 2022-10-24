package com.github.adamr22.bedtime.presentation.views

import android.media.RingtoneManager
import android.os.Bundle
import android.util.Log
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
import com.github.adamr22.data.models.AlarmItemModel
import com.github.adamr22.bedtime.presentation.viewmodels.BedTimeViewModel
import com.github.adamr22.data.entities.AlarmAndDay
import com.github.adamr22.utils.PickAlarmInterface
import com.github.adamr22.utils.TimePicker
import com.github.adamr22.utils.VibrateSingleton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.flow.collectLatest

class BedTImeFragmentBottomSheet(val viewModel: BedTimeViewModel) : BottomSheetDialogFragment() {

    private val BEDTIME_TAG = "Bedtime"
    private val WAKEUP_TAG = "Wakeup"

    private var BEDTIME_LABEL = ""
    private var WAKEUP_LABEL = ""

    private val SELECT_TIME_TAG = "Select Time"

    private val TAG = "BedTImeFragmentBottomSh"

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
        BEDTIME_LABEL = resources.getString(R.string.bedtime_capitalized)
        WAKEUP_LABEL = resources.getString(R.string.wake_up)

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
    }

    override fun onResume() {
        super.onResume()
        renderData()

        scheduleTime.setOnCheckedChangeListener { _, isChecked ->

        }

        reminderNotificationText.setOnClickListener {
//            NotificationReminderDialog(viewModel, data!!).show(
//                parentFragmentManager,
//                NotificationReminderDialog.TAG
//            )
        }

        btnSunriseAlarm.setOnCheckedChangeListener { _, isChecked ->

        }

        btnVibrate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // TODO: Update DB
            }

            if (!isChecked) {
                // TODO: Update DB
            }
        }

        tvDefaultSound.setOnClickListener {
            pickAlarmInterface.selectAlarmTone()
        }

        if (pickAlarmInterface.returnSelectedTone() != null) {
            val title = pickAlarmInterface.returnSelectedTone()!!.second
            val uri = pickAlarmInterface.returnSelectedTone()!!.first
            tvDefaultSound.text = title
            // TODO: Update DB
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
            // TODO: Update DB with selected time
        }

        super.onResume()
    }

    private fun renderUI(data: AlarmItemModel?) {
        renderBottomSheetText(data)
    }

    private fun renderBottomSheetText(data: AlarmItemModel?) {
        if (inflateBedTimeLayout!!) {
            bottomSheetIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_bedtime
                )
            )
            bottomSheetText.text = resources.getString(R.string.bedtime_capitalized)

            data?.let {
                renderViewText(tvSetTime, it.hour, it.minute)
            }

            bedtimeNotTextContent.visibility = View.VISIBLE

            renderBedtimeTextContent(data)
        } else bedtimeNotTextContent.visibility = View.GONE

        if (inflateWakeUpLayout!!) {
            bottomSheetIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_bed
                )
            )
            bottomSheetText.text = resources.getString(R.string.wake_up)

            data?.let {
                renderViewText(tvSetTime, it.hour, it.minute)
            }

            wakeUpNotTextContent.visibility = View.VISIBLE
            renderWakeUpTextContent(data)
        } else wakeUpNotTextContent.visibility = View.GONE
    }

    private fun renderData() {
        var item: AlarmItemModel?

        if (inflateBedTimeLayout!!) {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.getBedtime(BEDTIME_LABEL).collectLatest {
                    it?.let {
                        item = encapsulateData(it)
                        renderUI(item)
                        switchIsChecked(item)
                        changeNotificationReminderText(item)
                    }
                }
            }
        }

        if (inflateWakeUpLayout!!) {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.getWakeup(WAKEUP_LABEL).collectLatest {
                    it?.let {
                        item = encapsulateData(it)
                        renderUI(item)
                        isSunrise(item)
                        selectedSound(item)
                        isVibrate(item)
                        switchIsChecked(item)
                    }
                }
            }
        }
    }

    private fun renderBedtimeTextContent(data: AlarmItemModel?) {
        when (data?.isScheduled) {
            true -> {
                bedtimeNotTextContent.visibility = View.GONE
                bedtimeTextContent.visibility = View.VISIBLE
            }

            false -> {
                bedtimeTextContent.visibility = View.GONE
                bedtimeNotTextContent.visibility = View.VISIBLE
            }

            else -> {}
        }
    }

    private fun isSunrise(data: AlarmItemModel?) {
        data?.let {
            btnSunriseAlarm.isChecked = it.isSunriseMode
        }

        if (data == null) btnSunriseAlarm.isChecked = false
    }

    private fun isVibrate(data: AlarmItemModel?) {
        data?.let {
            btnVibrate.isChecked = it.isVibrate
        }

        if (data == null) btnSunriseAlarm.isChecked = false
    }

    private fun switchIsChecked(data: AlarmItemModel?) {
        data?.let {
            scheduleTime.isChecked = it.isScheduled
            tvSetTime.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }

        if (data == null) scheduleTime.isChecked = false

    }

    private fun selectedSound(data: AlarmItemModel?) {
        data?.let {
            if (it.ringtoneTitle != null) tvDefaultSound.text = it.ringtoneTitle
        }

        if (data == null) tvDefaultSound.text = defaultRingtoneTitle
    }

    private fun renderWakeUpTextContent(data: AlarmItemModel?) {

        when (data?.isScheduled) {
            true -> {
                wakeUpNotTextContent.visibility = View.GONE
                wakeUpTextContent.visibility = View.VISIBLE
            }

            false -> {
                wakeUpTextContent.visibility = View.GONE
                wakeUpNotTextContent.visibility = View.VISIBLE
            }

            else -> {}
        }
    }

    private fun changeNotificationReminderText(data: AlarmItemModel?) {
        data?.let {
            reminderNotificationSetTime.text = when (it.reminder) {
                1 -> resources.getString(R.string.one_hr_reminder)
                15 -> resources.getString(R.string.fifteen_min_reminder)
                30 -> resources.getString(R.string.thirty_min_reminder)
                45 -> resources.getString(R.string.forty_five_min_reminder)
                else -> return
            }
        }
    }

    private fun encapsulateData(it: AlarmAndDay): AlarmItemModel {
        val schedule = ArrayList<String>()

        it.dayOfWeek.forEach {
            if (it.day != null) schedule.add(it.day!!)
        }

        Log.d(TAG, "encapsulateData: Data: $it")

        return AlarmItemModel(
            it.alarm.id!!,
            it.alarm.label,
            it.alarm.hour,
            it.alarm.minute,
            schedule,
            it.alarm.title,
            it.alarm.uri,
            it.alarm.isScheduled,
            it.alarm.reminder,
            it.alarm.vibrates,
            it.alarm.sunriseMode
        )
    }

    private fun renderViewText(textView: TextView, hour: Int, minute: Int) {
        textView.text = String.format(
            resources.getString(R.string.default_time_2),
            "%02d".format(hour),
            "%02d".format(minute)
        )
    }


}