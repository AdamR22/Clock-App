package com.github.adamr22.timer.presentation.views

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.github.adamr22.R
import com.github.adamr22.common.AddLabelDialog
import com.github.adamr22.timer.presentation.viewmodels.TimerViewModel
import com.github.adamr22.timer.data.models.TimerModel
import com.github.adamr22.timer.presentation.adapters.RunFragmentViewPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest

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

    private lateinit var animation: Animation

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

        animation = AnimationUtils.loadAnimation(requireContext(), R.anim.blink)

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

        timeRemaining =
            timerViewModel.convertTimeToMilliseconds(timerModel.setTime) + 1000 // "Hack" preventing timer from starting a second too early

        pbTimer.max = timerViewModel.convertTimeToMilliseconds(timerModel.setTime).toInt()

        updateTimeText(time)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        Log.d(TAG, "onResume: $position")
        monitorLabelChange()
        renderUIAccordingToTimerState()
        runTimer(timeRemaining)

        addLabel.setOnClickListener {
            AddLabelDialog.newInstance(position, null, timerViewModel).show(mFragmentManager, null)
        }

        btnDeleteTimer.setOnClickListener {
            tvSetTime.clearAnimation()
            timerModel.timer?.cancel()
            fragAdapter.notifyItemRemoved(position)
            timerViewModel.deleteTimer(position)
        }

        btnAddTimer.setOnClickListener {
            mFragmentManager.popBackStack()
        }

        btnPauseTimer.setOnClickListener {
            timerModel.timer?.cancel()
            timerViewModel.changeTimerState(
                position,
                TimerViewModel.TimerStates.PAUSED
            )
        }

        btnPlayTimer.setOnClickListener {
            timerViewModel.changeTimerState(
                position,
                TimerViewModel.TimerStates.RUNNING
            )

            runTimer(timeRemaining)
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

            tvSetTime.text = String.format(
                resources.getString(R.string.running_timer_1_text),
                hourText,
                minuteText,
                secondText
            )
        }

        if (time.first == 0L && time.second != 0L) {
            val minuteText = "%02d".format(time.second.toInt())
            val secondText = "%02d".format(time.third.toInt())

            tvSetTime.text = String.format(
                resources.getString(R.string.running_timer_2_text),
                minuteText,
                secondText
            )
        }

        if (time.first == 0L && time.second == 0L) {
            val secondText = "%02d".format(time.third.toInt())

            tvSetTime.text =
                String.format(resources.getString(R.string.running_timer_3_text), secondText)
        }
    }

    private fun runTimer(time: Long) {
        tvSetTime.clearAnimation()
        timerViewModel.updateRemainingTime(position, time)

        timerModel.timer = object : CountDownTimer(time, 1000) {
            override fun onTick(timeRemainingUntilFinished: Long) {
                timeRemaining = timeRemainingUntilFinished
                timerViewModel.updateRemainingTime(position, timeRemaining)
                updateTimeText(
                    timerViewModel.convertMillisecondsToHoursMinutesAndSeconds(
                        timeRemainingUntilFinished
                    )
                )
                Log.d(TAG, "onTick: $timeRemaining")
                updateProgressBar(timeRemaining - 1000)
            }

            override fun onFinish() {
                // TODO: Function to show notification and also play timer song
                timerModel.timer?.cancel()
                timerViewModel.changeTimerState(position, TimerViewModel.TimerStates.FINISHED)
                Toast.makeText(requireContext(), "timer finished", Toast.LENGTH_SHORT)
                    .show() // Placeholder toast notification
            }
        }.start()
    }

    private fun updateProgressBar(timeRemaining: Long) {
        pbTimer.progress = timeRemaining.toInt()
    }

    private fun renderUIAccordingToTimerState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            timerViewModel.timerState.collectLatest {
                if (it is TimerViewModel.TimerState.Changed) {
                    if (it.state == TimerViewModel.TimerStates.PAUSED || it.state == TimerViewModel.TimerStates.FINISHED) {
                        tvSetTime.startAnimation(animation)
                        tvAddOneMinOrReset.text = resources.getString(R.string.reset)
                        btnPlayTimer.visibility = View.VISIBLE
                        btnPauseTimer.visibility = View.GONE
                    }

                    if (it.state == TimerViewModel.TimerStates.RUNNING) {
                        tvSetTime.clearAnimation()
                        tvAddOneMinOrReset.text = resources.getString(R.string.add_1_min)
                        btnPlayTimer.visibility = View.GONE
                        btnPauseTimer.visibility = View.VISIBLE
                    }

                    if (it.state == TimerViewModel.TimerStates.FINISHED) {
                        updateTimeText(
                            timerViewModel.convertMillisecondsToHoursMinutesAndSeconds(
                                timerViewModel.convertTimeToMilliseconds(timerModel.setTime)
                            )
                        )

                        timeRemaining = timerViewModel.convertTimeToMilliseconds(timerModel.setTime) + 1000
                    }
                }
            }
        }
    }

    private fun monitorLabelChange() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            timerViewModel.timerLabelState.collectLatest {
                if (it is TimerViewModel.TimerLabelState.Changed) addLabel.text = timerModel.label
            }
        }
    }
}