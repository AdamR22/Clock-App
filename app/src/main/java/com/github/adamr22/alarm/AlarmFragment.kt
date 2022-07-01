package com.github.adamr22.alarm

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R
import com.github.adamr22.common.TimePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker

class AlarmFragment : Fragment() {
    
    private lateinit var addAlarmButton: FloatingActionButton
    private lateinit var picker: MaterialTimePicker
    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var alarmRecyclerView: RecyclerView
    private lateinit var emptyAlarmContent: ConstraintLayout

    private val TAG = "AlarmFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addAlarmButton = view.findViewById(R.id.fab_add)
        alarmRecyclerView = view.findViewById(R.id.rv_alarm_items)
        emptyAlarmContent = view.findViewById(R.id.empty_alarm_content)
        picker = TimePicker.buildPicker("Set Alarm")
    }

    override fun onResume() {
        super.onResume()
        // TODO: Add Alarm functionality here
        addAlarmButton.setOnClickListener {
            picker.show(parentFragmentManager, "Alarm Picker")

            picker.addOnPositiveButtonClickListener {
                addAlarmItem()
                Log.d(TAG, "onResume: ${picker.hour}:${picker.minute}")
            }
        }

        alarmViewModel.getAlarmItems().observe(viewLifecycleOwner) {
            toggleLayoutType(it)
        }
    }

    private fun toggleLayoutType(data: ArrayList<AlarmItemModel>) {
        if (data.isNotEmpty()) {
            alarmRecyclerView.visibility = View.VISIBLE
            emptyAlarmContent.visibility = View.GONE
        } else {
            alarmRecyclerView.visibility = View.GONE
            emptyAlarmContent.visibility = View.VISIBLE
        }
    }

    private fun addAlarmItem() {
        val chosenTime = "${picker.hour}:${picker.minute}"
        val alarm = AlarmItemModel(null, chosenTime, null)
        alarmViewModel.addAlarmItem(alarm)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AlarmFragment()
    }
}