package com.github.adamr22.timer

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.adamr22.R
import kotlinx.coroutines.flow.collectLatest
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.util.Calendar

class RunTimerFragment : Fragment() {

    private val TAG = "RunTimerFragment"

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerViewAdapter: RunTimerAdapter
    private lateinit var mRecyclerViewIndicator: ScrollingPagerIndicator
    private var timerViewModel: TimerViewModel = TimerViewModel()

    fun passViewModel(viewModel: TimerViewModel) {
        timerViewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_run_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mRecyclerView = view.findViewById(R.id.rv_run_timer)
        mRecyclerViewAdapter =
            RunTimerAdapter(requireContext(), parentFragmentManager, timerViewModel)
        mRecyclerViewIndicator = view.findViewById(R.id.set_timer_indicator)

        mRecyclerView.adapter = mRecyclerViewAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        mRecyclerViewIndicator.attachToRecyclerView(mRecyclerView)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        // Changes indicator value
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            timerViewModel.timers.collectLatest {
                when (it) {
                    is TimerViewModel.TimerFragmentUIState.Timers -> {
                        mRecyclerViewAdapter.updateList(it.timerInstances)
                        runTimers(it.timerInstances)
                    }
                    else -> {}
                }
            }
        }
        super.onResume()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RunTimerFragment()
    }

    private fun runTimers(timers: List<TimerModel>) {
        if (timers.isEmpty()) return

        timers.forEach { timer ->
            val timerPosition = timers.indexOf(timer)

            val timeInMilliseconds = timerViewModel.convertTimeToMilliseconds(timer.setTime)

            timerViewModel.updateRemainingTime(timerPosition, timeInMilliseconds)

            timer.timer = object : CountDownTimer(timeInMilliseconds, 1000) {
                override fun onTick(timerUntilFinished: Long) {
                    timerViewModel.updateRemainingTime(timerPosition, timerUntilFinished)
                }

                override fun onFinish() {
                    timer.timer?.cancel()
                }
            }.start()
        }
    }

}