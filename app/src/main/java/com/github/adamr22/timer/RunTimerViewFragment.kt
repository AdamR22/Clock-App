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
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.adamr22.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RunTimerViewFragment(
    private val timerModel: TimerModel,
    private val position: Int,
    private val timerViewModel: TimerViewModel,
    val mFragmentManager: FragmentManager,
    private val fragAdapter: FragmentStateAdapter
) : Fragment() {

    private val TAG = "RunTimerViewFragment"


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
        runTimer()

        btnDeleteTimer.setOnClickListener {
            Log.d(TAG, "onResume: Deleted Position $position")
            timerViewModel.deleteTimer(position)
            fragAdapter.notifyItemRemoved(position)
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
            position: Int,
            viewModel: TimerViewModel,
            fragManager: FragmentManager,
            adapter: FragmentStateAdapter
        ) =
            RunTimerViewFragment(
                data,
                position,
                viewModel,
                fragManager,
                adapter
            )
    }

    private fun runTimer() {
        val timeRemaining = timerViewModel.convertTimeToMilliseconds(timerModel.setTime)
        timerViewModel.updateRemainingTime(position, timeRemaining)
    }
}