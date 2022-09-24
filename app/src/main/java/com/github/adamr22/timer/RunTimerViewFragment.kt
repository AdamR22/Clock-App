package com.github.adamr22.timer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.github.adamr22.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RunTimerViewFragment(
    private val timerModel: TimerModel,
    private val timerViewModel: TimerViewModel,
    private val mFragmentManager: FragmentManager,
    private val fragAdapter: RunFragmentViewPagerAdapter
) : Fragment() {

    private val TAG = "RunTimerViewFragment"

    private var timeRemaining = 0L
    private var position = 0

    fun setPos(pos: Int) { position = pos }

    private lateinit var addLabel: TextView
    private lateinit var tvSetTime: TextView
    private lateinit var tvAddOneMinOrReset: TextView

    private lateinit var btnPlayTimer: FloatingActionButton
    private lateinit var btnDeleteTimer: FloatingActionButton
    private lateinit var btnAddTimer: FloatingActionButton
    private lateinit var btnPauseTimer: ImageButton
    private lateinit var pbTimer: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.run_timer_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        btnPlayTimer = view.findViewById(R.id.play)
        btnDeleteTimer = view.findViewById(R.id.btn_delete_timer)
        btnAddTimer = view.findViewById(R.id.btn_add_timer)
        btnPauseTimer = view.findViewById(R.id.btn_pause)
        pbTimer = view.findViewById(R.id.pb_timer)

        addLabel = view.findViewById(R.id.tv_timer_label)
        tvSetTime = view.findViewById(R.id.tv_set_timer)
        tvAddOneMinOrReset = view.findViewById(R.id.tv_add_one_minute)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        Log.d(TAG, "onResume: $position")
        runTimer()

        btnDeleteTimer.setOnClickListener {
            timerModel.timer?.cancel()
            timerViewModel.deleteTimer(position, fragAdapter)
        }

        btnAddTimer.setOnClickListener {
            mFragmentManager.popBackStack()
        }

        super.onResume()
    }

    companion object {
        @JvmStatic
        fun newInstance(
            data: TimerModel,
            viewModel: TimerViewModel,
            fragManager: FragmentManager,
            adapter: RunFragmentViewPagerAdapter
        ) =
            RunTimerViewFragment(
                data,
                viewModel,
                fragManager,
                adapter
            )
    }

    private fun runTimer() {
        timeRemaining = timerViewModel.convertTimeToMilliseconds(timerModel.setTime)
        timerViewModel.updateRemainingTime(position, timeRemaining)
    }
}