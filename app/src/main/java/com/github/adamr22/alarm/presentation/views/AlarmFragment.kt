package com.github.adamr22.alarm.presentation.views

import android.media.RingtoneManager
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R
import com.github.adamr22.alarm.data.models.AlarmItemModel
import com.github.adamr22.alarm.presentation.adapters.AlarmRecyclerViewAdapter
import com.github.adamr22.alarm.presentation.viewmodels.AlarmViewModel
import com.github.adamr22.common.TimePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.coroutines.flow.collectLatest

class AlarmFragment : Fragment() {

    private lateinit var addAlarmButton: FloatingActionButton
    private lateinit var picker: MaterialTimePicker
    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var alarmRecyclerView: RecyclerView
    private lateinit var emptyAlarmContent: ConstraintLayout
    private lateinit var alarmAdapter: AlarmRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alarmViewModel = ViewModelProvider(requireActivity())[AlarmViewModel::class.java]
        addAlarmButton = view.findViewById(R.id.fab_add)
        alarmRecyclerView = view.findViewById(R.id.rv_alarm_items)
        emptyAlarmContent = view.findViewById(R.id.empty_alarm_content)

        picker = TimePicker.buildPicker("Set Alarm")

        alarmAdapter = AlarmRecyclerViewAdapter(requireContext(), alarmViewModel)
        alarmRecyclerView.adapter = alarmAdapter
        alarmRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        alarmRecyclerView.hasFixedSize()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            alarmViewModel.alarmItems.collectLatest {
                when (it) {
                    is AlarmViewModel.AlarmUIState.AlarmItems -> {
                        alarmRecyclerView.visibility = View.VISIBLE
                        emptyAlarmContent.visibility = View.GONE
                        alarmAdapter.updateAlarmList(it.alarmItems)
                    }

                    is AlarmViewModel.AlarmUIState.Empty -> {
                        alarmRecyclerView.visibility = View.GONE
                        emptyAlarmContent.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity).supportActionBar?.title = "Alarm"

        addAlarmButton.setOnClickListener {
            picker.show(parentFragmentManager, "Alarm Picker")

            picker.addOnPositiveButtonClickListener {
                addAlarmItem()
            }
        }
    }

    private fun addAlarmItem() {
        val chosenTime = "%02d:%02d".format(picker.hour, picker.minute)
        val alarm = AlarmItemModel(
            chosenTime,
            RingtoneManager.getRingtone(
                requireActivity(),
                Settings.System.DEFAULT_ALARM_ALERT_URI
            ).getTitle(requireContext()),
            RingtoneManager.getActualDefaultRingtoneUri(
                requireContext(),
                RingtoneManager.TYPE_ALARM
            ),
        )
        alarmViewModel.addAlarmItem(alarm)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AlarmFragment()
    }

}