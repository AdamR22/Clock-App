package com.github.adamr22.clockapp.bedtime.presentation.views

import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.adamr22.clockapp.R
import com.github.adamr22.clockapp.bedtime.presentation.viewmodels.BedTimeViewModel
import com.github.adamr22.clockapp.data.entities.Alarm
import com.github.adamr22.clockapp.data.models.AlarmItemModel
import com.github.adamr22.clockapp.utils.PickAlarmInterface
import com.github.adamr22.clockapp.utils.TimePicker
import com.github.adamr22.clockapp.utils.VibrateSingleton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.flow.collectLatest

class BedTImeFragmentBottomSheet : BottomSheetDialogFragment() {

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

    private lateinit var wakeUpTextContent: ConstraintLayout
    private lateinit var btnSunriseAlarm: SwitchMaterial
    private lateinit var tvDefaultSound: TextView
    private lateinit var btnVibrate: SwitchMaterial

    private lateinit var dailyCheckbox: CheckBox

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

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[BedTimeViewModel::class.java]
    }

    private fun storagePermissionGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                pickAlarmInterface.selectAlarmTone()
            }
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

        wakeUpTextContent = view.findViewById(R.id.wakeup_text_content)
        btnSunriseAlarm = view.findViewById(R.id.btn_sunrise_alarm)
        tvDefaultSound = view.findViewById(R.id.tv_default_sound)
        btnVibrate = view.findViewById(R.id.btn_vibrate)

        dailyCheckbox = view.findViewById(R.id.day_picker_checkout)

        tvSetTime = view.findViewById(R.id.tv_set_time)
    }

    override fun onResume() {
        super.onResume()
        renderData()

        super.onResume()
    }

    private fun renderUI(data: AlarmItemModel?) {
        data?.let {
            dailyCheckbox.isChecked = it.everyDay
        }
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
                        // Feeds data to helper functions since updating data with outside function yields null
                        item = encapsulateData(it)
                        renderUI(item)
                        dailyCheck(item!!)
                        switchIsChecked(item)
                        schedulesTime(item!!)
                        updateSetTime(item)
                    }

                    if (it == null) {
                        updateSetTime(null)
                        renderUI(null)
                    }
                }
            }
        }

        if (inflateWakeUpLayout!!) {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.getWakeup(WAKEUP_LABEL).collectLatest {
                    it?.let {
                        // Feeds data to helper functions since updating data with outside function yields null
                        item = encapsulateData(it)
                        renderUI(item)
                        dailyCheck(item!!)
                        isSunrise(item)
                        selectedSound(item)
                        isVibrate(item)
                        switchIsChecked(item)
                        schedulesTime(item!!)
                        updateSetTime(item)
                        selectSong(item!!)
                        setSunriseMode(item!!)
                        setVibrationMode(item!!)
                    }

                    if (it == null) {
                        updateSetTime(null)
                        renderUI(null)
                        isSunrise(null)
                        selectedSound(null)
                        isVibrate(null)
                        switchIsChecked(null)
                    }
                }
            }
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

    private fun encapsulateData(it: Alarm): AlarmItemModel {

        return AlarmItemModel(
            it.id!!,
            it.label,
            it.hour,
            it.minute,
            it.title,
            it.uri,
            it.isScheduled,
            it.vibrates,
            it.sunriseMode,
            it.expandedItem,
            it.everyDay
        )
    }

    private fun renderViewText(textView: TextView, hour: Int, minute: Int) {
        textView.text = String.format(
            resources.getString(R.string.default_time_2),
            "%02d".format(hour),
            "%02d".format(minute)
        )
    }

    private fun updateSetTime(data: AlarmItemModel?) {
        tvSetTime.setOnClickListener {
            timePicker.show(parentFragmentManager, TAG)
            timePicker.addOnPositiveButtonClickListener {
                tvSetTime.text = String.format(
                    resources.getString(R.string.default_time_2),
                    "%02d".format(timePicker.hour),
                    "%02d".format(timePicker.minute)
                )

                if (data == null) {
                    viewModel.insertTime(
                        Alarm(
                            label = bottomSheetText.text.toString(),
                            hour = timePicker.hour,
                            minute = timePicker.minute,
                            title = defaultRingtoneTitle,
                            uri = defaultRingtoneUri
                        )
                    )
                } else {
                    viewModel.updateTime(
                        Alarm(
                            data.id,
                            data.label,
                            data.ringtoneTitle,
                            data.ringtoneUri,
                            data.isScheduled,
                            data.isSunriseMode,
                            data.isVibrate,
                            data.expandedItem,
                            data.everyDay,
                            timePicker.hour,
                            timePicker.minute,
                        )
                    )
                }

            }
        }
    }

    private fun schedulesTime(data: AlarmItemModel) {
        scheduleTime.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.updateTime(
                    Alarm(
                        data.id,
                        data.label,
                        data.ringtoneTitle,
                        data.ringtoneUri,
                        true,
                        data.isSunriseMode,
                        data.isVibrate,
                        data.expandedItem,
                        data.everyDay,
                        data.hour,
                        data.minute,
                    )
                )
            }

            if (!isChecked) {
                viewModel.updateTime(
                    Alarm(
                        data.id,
                        data.label,
                        data.ringtoneTitle,
                        data.ringtoneUri,
                        false,
                        data.isSunriseMode,
                        data.isVibrate,
                        data.expandedItem,
                        data.everyDay,
                        data.hour,
                        data.minute,
                    )
                )
            }
        }
    }

    private fun setSunriseMode(data: AlarmItemModel) {
        btnSunriseAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.updateTime(
                    Alarm(
                        data.id,
                        data.label,
                        data.ringtoneTitle,
                        data.ringtoneUri,
                        data.isScheduled,
                        true,
                        data.isVibrate,
                        data.expandedItem,
                        data.everyDay,
                        data.hour,
                        data.minute,
                    )
                )
            }

            if (!isChecked) {
                viewModel.updateTime(
                    Alarm(
                        data.id,
                        data.label,
                        data.ringtoneTitle,
                        data.ringtoneUri,
                        data.isScheduled,
                        false,
                        data.isVibrate,
                        data.expandedItem,
                        data.everyDay,
                        data.hour,
                        data.minute,
                    )
                )
            }
        }
    }

    private fun selectSong(data: AlarmItemModel) {
        tvDefaultSound.setOnClickListener {
            if (storagePermissionGranted()) {
                pickAlarmInterface.selectAlarmTone()
            }

            if (!storagePermissionGranted()) {
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        if (pickAlarmInterface.returnSelectedTone() != null) {
            val title = pickAlarmInterface.returnSelectedTone()!!.second
            val uri = pickAlarmInterface.returnSelectedTone()!!.first

            viewModel.updateTime(
                Alarm(
                    data.id,
                    data.label,
                    title,
                    uri,
                    data.isScheduled,
                    data.isSunriseMode,
                    data.isVibrate,
                    data.expandedItem,
                    data.everyDay,
                    data.hour,
                    data.minute,
                )
            )
        }
    }

    private fun setVibrationMode(data: AlarmItemModel) {
        btnVibrate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                VibrateSingleton.vibrateDeviceOnce(requireContext(), true)
                viewModel.updateTime(
                    Alarm(
                        data.id,
                        data.label,
                        data.ringtoneTitle,
                        data.ringtoneUri,
                        data.isScheduled,
                        data.isSunriseMode,
                        true,
                        data.expandedItem,
                        data.everyDay,
                        data.hour,
                        data.minute,
                    )
                )
            }

            if (!isChecked) {
                viewModel.updateTime(
                    Alarm(
                        data.id,
                        data.label,
                        data.ringtoneTitle,
                        data.ringtoneUri,
                        data.isScheduled,
                        data.isSunriseMode,
                        false,
                        data.expandedItem,
                        data.everyDay,
                        data.hour,
                        data.minute,
                    )
                )
            }
        }
    }

    private fun dailyCheck(item: AlarmItemModel) {
        dailyCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.updateDaily(true, item.id)

            if (!isChecked) viewModel.updateDaily(false, item.id)
        }
    }
}
