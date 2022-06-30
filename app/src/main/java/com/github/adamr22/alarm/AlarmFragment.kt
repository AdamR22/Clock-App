package com.github.adamr22.alarm

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.adamr22.R
import com.github.adamr22.common.TimePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker

class AlarmFragment : Fragment() {
    
    lateinit var addAlarmButton: FloatingActionButton
    lateinit var picker: MaterialTimePicker

    private val TAG = "AlarmFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        picker = TimePicker.buildPicker("Set Alarm")
    }

    override fun onResume() {
        super.onResume()
        // TODO: Add Alarm functionality here
        addAlarmButton.setOnClickListener {
            picker.show(parentFragmentManager, "Alarm Picker")

            picker.addOnPositiveButtonClickListener {
                Log.d(TAG, "onResume: ${picker.hour}:${picker.minute}")
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AlarmFragment()
    }
}