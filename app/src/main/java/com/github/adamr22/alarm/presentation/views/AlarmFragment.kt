package com.github.adamr22.alarm.presentation.views

import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R
import com.github.adamr22.alarm.presentation.adapters.AlarmRecyclerViewAdapter
import com.github.adamr22.alarm.presentation.viewmodels.AlarmViewModel
import com.github.adamr22.data.entities.Alarm
import com.github.adamr22.utils.TimePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.coroutines.flow.collectLatest

class AlarmFragment : Fragment() {

    private lateinit var addAlarmButton: FloatingActionButton
    private val picker: MaterialTimePicker = TimePicker.buildPicker("Set Alarm")
    private lateinit var alarmRecyclerView: RecyclerView
    private lateinit var emptyAlarmContent: ConstraintLayout
    private lateinit var alarmAdapter: AlarmRecyclerViewAdapter

    private val defaultRingtoneUri by lazy {
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
    }

    private val defaultRingtoneTitle by lazy {
        RingtoneManager.getRingtone(requireContext(), defaultRingtoneUri).getTitle(requireContext())
    }

    private val alarmViewModel by lazy {
        ViewModelProvider(requireActivity())[AlarmViewModel::class.java]
    }

    private fun storagePermissionGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            picker.show(parentFragmentManager, "Alarm Picker")

            picker.addOnPositiveButtonClickListener {
                addAlarmItem()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.alarm)
        return inflater.inflate(R.layout.fragment_alarm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addAlarmButton = view.findViewById(R.id.fab_add)
        alarmRecyclerView = view.findViewById(R.id.rv_alarm_items)
        emptyAlarmContent = view.findViewById(R.id.empty_alarm_content)

        alarmAdapter = AlarmRecyclerViewAdapter(requireContext(), alarmViewModel)
        alarmRecyclerView.adapter = alarmAdapter
        alarmRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        alarmRecyclerView.hasFixedSize()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            alarmViewModel.getAllDataApartFromBedtime().collectLatest {
                it?.let {
                    emptyAlarmContent.visibility = View.GONE
                    alarmRecyclerView.visibility = View.VISIBLE
                    alarmAdapter.data.submitList(it)
                }

                if (it == null || it.isEmpty()) {
                    emptyAlarmContent.visibility = View.VISIBLE
                    alarmRecyclerView.visibility = View.GONE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        addAlarmButton.setOnClickListener {

            if (!storagePermissionGranted()) {
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            } else if (storagePermissionGranted()) {
                picker.show(parentFragmentManager, "Alarm Picker")

                picker.addOnPositiveButtonClickListener {
                    addAlarmItem()
                }
            }
        }
    }

    private fun addAlarmItem() {
        alarmViewModel.insertAlarm(
            Alarm(
                title = defaultRingtoneTitle,
                uri = defaultRingtoneUri,
                hour = picker.hour,
                minute = picker.minute
            )
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AlarmFragment()
    }

}