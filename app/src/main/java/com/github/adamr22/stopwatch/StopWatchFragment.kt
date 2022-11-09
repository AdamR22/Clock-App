package com.github.adamr22.stopwatch

import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.adamr22.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest

class StopWatchFragment : Fragment() {

    var stateOrdinalValue = 0

    private val STATE_ORDINAL_SHARED_PREF = "State Ordinal Value"
    private val STATE_ORDINAL_KEY = "Current State Ordinal Value"

    private val sharedPref by lazy {
        requireContext().getSharedPreferences(STATE_ORDINAL_SHARED_PREF, MODE_PRIVATE)
    }

    private val animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.blink)
    }

    companion object {
        fun newInstance() = StopWatchFragment()
    }

    private lateinit var adapter: ArrayAdapter<String>

    private lateinit var btnReset: FloatingActionButton
    private lateinit var btnPause: ImageButton
    private lateinit var btnPlay: FloatingActionButton
    private lateinit var btnAddLap: FloatingActionButton

    private lateinit var stopwatchTime: TextView

    private lateinit var viewModel: StopWatchViewModel
    private lateinit var lapTimeLists: ListView


    private val timeStamps = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[StopWatchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireContext() as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.stopwatch)

        return inflater.inflate(R.layout.fragment_stop_watch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnReset = view.findViewById(R.id.btn_reset_stopwatch)
        btnAddLap = view.findViewById(R.id.btn_add_lap)
        btnPlay = view.findViewById(R.id.play)
        btnPause = view.findViewById(R.id.btn_pause)

        stopwatchTime = view.findViewById(R.id.stopwatch_content)

        lapTimeLists = view.findViewById(R.id.lap_list)

        adapter = ArrayAdapter(
            requireContext(),
            R.layout.lap_item_layout,
            R.id.tv_lap_time,
            timeStamps
        )

        lapTimeLists.adapter = adapter

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        readLapTimeList()

        readStopWatchTime()

        val savedOrdinalValue = sharedPref.getInt(STATE_ORDINAL_KEY, -1)

        stateOrdinalValue = if (savedOrdinalValue > -1) savedOrdinalValue else 0

        lapTimeLists.visibility = if (timeStamps.isEmpty()) View.GONE else View.VISIBLE

        btnPause.setOnClickListener {
            stateOrdinalValue = 1
            viewModel.changeState(stateOrdinalValue)

            viewModel.cancelStopWatch()
        }

        btnPlay.setOnClickListener {
            stateOrdinalValue = 2
            viewModel.changeState(stateOrdinalValue)

            viewModel.runStopWatch()
        }

        btnReset.setOnClickListener {
            stateOrdinalValue = 0
            viewModel.resetStopWatch()
            viewModel.changeState(stateOrdinalValue)
        }

        btnAddLap.setOnClickListener {
            viewModel.lapTime()
        }

        viewModel.changeState(stateOrdinalValue)

        renderUI()
    }

    override fun onPause() {
        sharedPref.edit().putInt(STATE_ORDINAL_KEY, stateOrdinalValue).apply()
        super.onPause()
    }

    private fun renderUI() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.stopwatchState.collectLatest {
                when (it) {
                    StopWatchViewModel.StopWatchStates.INITIAL -> {
                        btnPause.visibility = View.GONE
                        btnAddLap.visibility = View.GONE
                        btnReset.visibility = View.GONE
                        btnPlay.visibility = View.VISIBLE
                        stopwatchTime.clearAnimation()
                        stopwatchTime.text = String.format(
                            resources.getString(R.string.running_stopwatch_1_text),
                            "00",
                            "00"
                        )
                    }
                    StopWatchViewModel.StopWatchStates.PAUSED -> {
                        btnPause.visibility = View.GONE
                        btnAddLap.visibility = View.GONE
                        btnReset.visibility = View.VISIBLE
                        btnPlay.visibility = View.VISIBLE
                        stopwatchTime.startAnimation(animation)
                    }
                    StopWatchViewModel.StopWatchStates.RUNNING -> {
                        btnPause.visibility = View.VISIBLE
                        btnAddLap.visibility = View.VISIBLE
                        btnReset.visibility = View.VISIBLE
                        btnPlay.visibility = View.GONE
                        stopwatchTime.clearAnimation()
                    }
                }
            }
        }
    }

    private fun readLapTimeList() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.lapTimes.collectLatest {

                if (it.isEmpty()) lapTimeLists.visibility = View.GONE

                if (it.isNotEmpty()) {
                    lapTimeLists.visibility = View.VISIBLE
                    timeStamps.clear()
                    it.forEach { lap ->

                        val formattedTimeList = viewModel.formatTime(lap)

                        val formattedLapTime = "#${it.indexOf(lap) + 1} ${formatLapTime(formattedTimeList)}"

                        timeStamps.add(formattedLapTime)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun readStopWatchTime() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.time.collectLatest {
                if (it.isEmpty()) stopwatchTime.text = String.format(
                    resources.getString(R.string.running_stopwatch_1_text),
                    "00",
                    "00"
                )

                if (it.isNotEmpty()) {
                    val formattedTimeList = viewModel.formatTime(it)

                    updateTimeText(formattedTimeList)
                }
            }
        }
    }

    private fun updateTimeText(time: List<Long>) {

        val hourText = "%02d".format(time[0].toInt())
        val minuteText = "%02d".format(time[1].toInt())
        val secondText = "%02d".format(time[2].toInt())
        val milliSecondText = "%02d".format(time[3].toInt())

        if (time[0] != 0L) {

            stopwatchTime.text = String.format(
                resources.getString(R.string.running_stopwatch_4_text),
                hourText,
                minuteText,
                secondText
            )
        }

        if (time[0] == 0L && time[1] != 0L) {

            if (time[1] >= 10) {
                stopwatchTime.text = String.format(
                    resources.getString(R.string.running_stopwatch_3_text),
                    minuteText,
                    secondText
                )
            }

            if (time[1] < 10) {
                stopwatchTime.text = String.format(
                    resources.getString(R.string.running_stopwatch_2_text),
                    minuteText,
                    secondText,
                    milliSecondText
                )
            }

        }

        if (time[0] == 0L && time[1] == 0L) {
            stopwatchTime.text = String.format(
                resources.getString(R.string.running_stopwatch_1_text),
                secondText,
                milliSecondText
            )
        }
    }

    private fun formatLapTime(time: List<Long>): String {

        var lapTime = ""

        val hourText = "%02d".format(time[0].toInt())
        val minuteText = "%02d".format(time[1].toInt())
        val secondText = "%02d".format(time[2].toInt())
        val milliSecondText = "%02d".format(time[3].toInt())

        if (time[0] != 0L) {

            lapTime = String.format(
                resources.getString(R.string.running_stopwatch_4_text),
                hourText,
                minuteText,
                secondText
            )
        }

        if (time[0] == 0L && time[1] != 0L) {

            if (time[1] >= 10) {
                lapTime = String.format(
                    resources.getString(R.string.running_stopwatch_3_text),
                    minuteText,
                    secondText
                )
            }

            if (time[1] < 10) {
                lapTime = String.format(
                    resources.getString(R.string.running_stopwatch_2_text),
                    minuteText,
                    secondText,
                    milliSecondText
                )
            }

        }

        if (time[0] == 0L && time[1] == 0L) {
            lapTime = String.format(
                resources.getString(R.string.running_stopwatch_1_text),
                secondText,
                milliSecondText
            )
        }

        return lapTime
    }
}