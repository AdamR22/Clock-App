package com.github.adamr22.timer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.github.adamr22.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SetTimerFragment : Fragment() {

    private lateinit var btnStartTimer: FloatingActionButton
    private lateinit var timerAdapter: SetTimerAdapter
    private lateinit var timerNumpad: GridView

    private val timerNumpadText = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "00", "0", "X")

    override fun onCreate(savedInstanceState: Bundle?) {
        timerAdapter = SetTimerAdapter(timerNumpadText, requireContext())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_set_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnStartTimer = view.findViewById(R.id.start_timer)
        timerNumpad = view.findViewById(R.id.timer_numpad)

        timerNumpad.adapter = timerAdapter

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SetTimerFragment()
    }
}