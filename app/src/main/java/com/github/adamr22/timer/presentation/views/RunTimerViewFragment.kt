package com.github.adamr22.timer.presentation.views

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.github.adamr22.R
import com.github.adamr22.timer.presentation.viewmodels.TimerViewModel
import com.github.adamr22.timer.data.models.TimerModel
import com.github.adamr22.timer.presentation.adapters.RunFragmentViewPagerAdapter
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

    fun setPos(pos: Int) {
        position = pos
    }

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

        val time = timerViewModel.convertMillisecondsToHoursMinutesAndSeconds(
            timerViewModel.convertTimeToMilliseconds(timerModel.setTime)
        )

        pbTimer.max = timerViewModel.convertTimeToMilliseconds(timerModel.setTime).toInt()

        updateTimeText(time)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        Log.d(TAG, "onResume: $position")
        runTimer()

        btnDeleteTimer.setOnClickListener {
            timerModel.timer?.cancel()
            fragAdapter.notifyItemRemoved(position)
            timerViewModel.deleteTimer(position)
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

    private fun updateTimeText(time: Triple<Long, Long, Long>) {
        if (time.first != 0L) {
            val hourText = "%02d".format(time.first.toInt())
            val minuteText = "%02d".format(time.second.toInt())
            val secondText = "%02d".format(time.third.toInt())

            tvSetTime.text = String.format(resources.getString(R.string.running_timer_1_text), hourText, minuteText, secondText)
        }

        if (time.first == 0L && time.second != 0L) {
            val minuteText = "%02d".format(time.second.toInt())
            val secondText = "%02d".format(time.third.toInt())

            tvSetTime.text = String.format(resources.getString(R.string.running_timer_2_text), minuteText, secondText)
        }

        if (time.first == 0L && time.second == 0L) {
            val secondText = "%02d".format(time.third.toInt())

            tvSetTime.text = String.format(resources.getString(R.string.running_timer_3_text), secondText)
        }
    }

    private fun runTimer() {
        timeRemaining = timerViewModel.convertTimeToMilliseconds(timerModel.setTime) + 1000 // "Hack" preventing timer from starting a second too early
        timerViewModel.updateRemainingTime(position, timeRemaining)

        timerModel.timer = object: CountDownTimer(timeRemaining, 1000) {
            override fun onTick(timeRemainingUntilFinished: Long) {
                timerViewModel.updateRemainingTime(position, timeRemainingUntilFinished)
                updateTimeText(timerViewModel.convertMillisecondsToHoursMinutesAndSeconds(timeRemainingUntilFinished))
                Log.d(TAG, "onTick: $timeRemainingUntilFinished")
                updateProgressBar(timeRemainingUntilFinished - 1000)
            }

            override fun onFinish() {
                // TODO: Function to show notification and also play timer song
                timerModel.timer?.cancel()
                Toast.makeText(requireContext(), "timer finished", Toast.LENGTH_SHORT).show() // Placeholder toast notification
            }
        }.start()
    }

    private fun updateProgressBar(timeRemaining: Long) {
        pbTimer.progress = timeRemaining.toInt()
    }
}