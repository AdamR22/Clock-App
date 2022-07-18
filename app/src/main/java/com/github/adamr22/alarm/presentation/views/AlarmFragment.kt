package com.github.adamr22.alarm.presentation.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R
import com.github.adamr22.alarm.data.models.AlarmItemModel
import com.github.adamr22.alarm.presentation.adapters.AlarmRecyclerViewAdapter
import com.github.adamr22.alarm.presentation.viewmodels.AlarmViewModel
import com.github.adamr22.common.TimePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker

class AlarmFragment : Fragment() {

    private lateinit var addAlarmButton: FloatingActionButton
    private lateinit var picker: MaterialTimePicker
    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var alarmRecyclerView: RecyclerView
    private lateinit var emptyAlarmContent: ConstraintLayout
    private lateinit var alarmAdapter: AlarmRecyclerViewAdapter

    private val TAG = "AlarmFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alarmViewModel = ViewModelProvider(requireActivity()).get(AlarmViewModel::class.java)
        alarmViewModel.getAlarmItems().observe(viewLifecycleOwner, updateListObserver())

        addAlarmButton = view.findViewById(R.id.fab_add)
        alarmRecyclerView = view.findViewById(R.id.rv_alarm_items)
        emptyAlarmContent = view.findViewById(R.id.empty_alarm_content)

        picker = TimePicker.buildPicker("Set Alarm")

        alarmAdapter = AlarmRecyclerViewAdapter(requireContext())
        alarmRecyclerView.adapter = alarmAdapter
        alarmRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        alarmRecyclerView.hasFixedSize()
    }

    override fun onResume() {
        super.onResume()

        addAlarmButton.setOnClickListener {
            picker.show(parentFragmentManager, "Alarm Picker")

            picker.addOnPositiveButtonClickListener {
                addAlarmItem()
            }
        }
    }

    private fun addAlarmItem() {
        val chosenTime = "${picker.hour}:${picker.minute}"
        val alarm = AlarmItemModel(null, chosenTime)
        alarmViewModel.addAlarmItem(alarm)
        Log.d(TAG, "addAlarmItem: Add Alarm Function Triggered")
        Log.d(TAG, "addAlarmItem: ${alarmViewModel.getAlarmItems().value?.size}")
        Log.d(TAG, "addAlarmItem: Chosen Time : $chosenTime")
    }

    private fun updateListObserver(): Observer<ArrayList<AlarmItemModel>> =
        Observer<ArrayList<AlarmItemModel>> { t ->
            Log.d(TAG, "updateListObserver: list size: ${t.size}")
            alarmAdapter.updateAlarmList(t)
            updateUILayout(t.size)
        }

    private fun updateUILayout(listSize: Int) {
        if (listSize > 0) {
            alarmRecyclerView.visibility = View.VISIBLE
            emptyAlarmContent.visibility = View.GONE
        } else {
            alarmRecyclerView.visibility = View.GONE
            emptyAlarmContent.visibility = View.VISIBLE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AlarmFragment()
    }
}